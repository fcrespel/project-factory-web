package com.cgi.projectfactory.api.config;

import java.util.Arrays;
import java.util.List;

import org.passay.CharacterCharacteristicsRule;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.UsernameRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.cgi.projectfactory.api.security.AuthorizationProperties;
import com.cgi.projectfactory.api.security.ResourceServerTokenServicesWrapper;

@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private ResourceServerProperties sso;

	@Autowired(required = false)
	@Qualifier("userInfoRestTemplate")
	private OAuth2RestOperations restTemplate;

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
					.anyRequest().authenticated();
	}

	@Bean
	public AuthorizationProperties authorizationProperties() {
		return new AuthorizationProperties();
	}

	@Bean
	public ResourceServerTokenServices resourceServerTokenServicesWrapper(AuthorizationProperties authorization) {
		ResourceServerTokenServicesWrapper services = new ResourceServerTokenServicesWrapper();
		services.setResourceServerTokenServices(userInfoTokenServices());
		services.setUserAttribute(authorization.getUserAttribute());
		services.setUserAuthorityMapper(userAuthorityMapper(authorization));
		services.setGroupAttribute(authorization.getGroupAttribute());
		services.setGroupAuthorityMapper(groupAuthorityMapper(authorization));
		services.setAuthorityMapping(authorization.getAuthorityMapping());
		return services;
	}

	public ResourceServerTokenServices userInfoTokenServices() {
		UserInfoTokenServices services = new UserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
		services.setRestTemplate(restTemplate);
		return services;
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
		charRule.getRules().add(new UppercaseCharacterRule());
		charRule.getRules().add(new LowercaseCharacterRule());
		charRule.getRules().add(new DigitCharacterRule());
		charRule.getRules().add(new SpecialCharacterRule());
		return Arrays.asList(lengthRule, usernameRule, charRule);
	}

}
