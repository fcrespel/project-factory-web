package fr.projectfactory.api.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.UsernameRule;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import fr.projectfactory.api.security.AuthoritiesExtractorImpl;
import fr.projectfactory.api.security.PrincipalExtractorImpl;
import lombok.Data;

@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	public static final String OAUTH_RESOURCE_ID = "platform-api";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(OAUTH_RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
				.authorizeRequests()
					.antMatchers("/v*/account/**").hasRole("USER")
					.antMatchers("/v*/directory/**").hasRole("ADMIN")
					.antMatchers("/v*/api-docs/**", "/swagger-resources/**").permitAll()
					.antMatchers("/", "/**/*.html", "/**/*.css", "/**/*.js", "/webjars/**").permitAll()
					.antMatchers("/management/health", "/management/info").permitAll()
					.antMatchers("/management/").hasRole("ADMIN")
					.anyRequest().hasRole("USER")
					.and()
				.httpBasic()
					.realmName(OAUTH_RESOURCE_ID);
	}

	@Bean
	public AuthorizationProperties authorizationProperties() {
		return new AuthorizationProperties();
	}

	@Data
	@ConfigurationProperties(prefix = "authorization")
	public class AuthorizationProperties {
		private String userAttribute;
		private String userAuthorityPrefix;
		private boolean userAuthorityUppercase;
		private String groupAttribute;
		private String groupAuthorityPrefix;
		private boolean groupAuthorityUppercase;
		private Set<String> defaultAuthorities;
		private Map<String, List<String>> authorityMapping;
	}

	@Bean
	public ResourceServerTokenServices userInfoTokenServices(ResourceServerProperties resource, UserInfoRestTemplateFactory restTemplateFactory, PrincipalExtractor principalExtractor, AuthoritiesExtractor authoritiesExtractor) {
		UserInfoTokenServices services = new UserInfoTokenServices(resource.getUserInfoUri(), resource.getClientId());
		services.setRestTemplate(restTemplateFactory.getUserInfoRestTemplate());
		services.setTokenType(resource.getTokenType());
		services.setPrincipalExtractor(principalExtractor);
		services.setAuthoritiesExtractor(authoritiesExtractor);
		return services;
	}

	@Bean
	public PrincipalExtractor principalExtractor(AuthorizationProperties authorization) {
		PrincipalExtractorImpl extractor = new PrincipalExtractorImpl();
		extractor.setUsernameAttribute(authorization.getUserAttribute());
		return extractor;
	}

	@Bean
	public AuthoritiesExtractor authoritiesExtractor(AuthorizationProperties authorization) {
		AuthoritiesExtractorImpl extractor = new AuthoritiesExtractorImpl();
		extractor.setUserAttribute(authorization.getUserAttribute());
		extractor.setUserAuthorityMapper(userAuthorityMapper(authorization));
		extractor.setGroupAttribute(authorization.getGroupAttribute());
		extractor.setGroupAuthorityMapper(groupAuthorityMapper(authorization));
		extractor.setDefaultAuthorities(authorization.getDefaultAuthorities());
		extractor.setAuthorityMapping(authorization.getAuthorityMapping());
		return extractor;
	}

	public GrantedAuthoritiesMapper userAuthorityMapper(AuthorizationProperties authorization) {
		if (authorization.getUserAuthorityPrefix() != null) {
			SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
			mapper.setConvertToUpperCase(authorization.isUserAuthorityUppercase());
			mapper.setPrefix(authorization.getUserAuthorityPrefix());
			return mapper;
		} else {
			return null;
		}
	}

	public GrantedAuthoritiesMapper groupAuthorityMapper(AuthorizationProperties authorization) {
		if (authorization.getGroupAuthorityPrefix() != null) {
			SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
			mapper.setConvertToUpperCase(authorization.isGroupAuthorityUppercase());
			mapper.setPrefix(authorization.getGroupAuthorityPrefix());
			return mapper;
		} else {
			return null;
		}
	}

	@Bean
	public PasswordValidator passwordValidator() {
		return new PasswordValidator(passwordRules());
	}

	public List<Rule> passwordRules() {
		LengthRule lengthRule = new LengthRule(8, Integer.MAX_VALUE);
		UsernameRule usernameRule = new UsernameRule(true, true);
		CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
		charRule.setNumberOfCharacteristics(3);
		charRule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase));
		charRule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase));
		charRule.getRules().add(new CharacterRule(EnglishCharacterData.Digit));
		charRule.getRules().add(new CharacterRule(EnglishCharacterData.Special));
		return Arrays.asList(lengthRule, usernameRule, charRule);
	}

}
