package fr.projectfactory.manager.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.util.UriComponentsBuilder;

import fr.projectfactory.manager.security.RevokeTokenLogoutHandler;
import lombok.Data;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2ProtectedResourceDetails resource;

	@Autowired
	private LogoutProperties logoutProperties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
				.authorizeRequests()
					.antMatchers("/login").permitAll()
					.antMatchers("/v1/**").permitAll() // API has its own security config
					.antMatchers("/**/*.css", "/**/*.js", "/webjars/**").permitAll()
					.antMatchers("/management/health", "/management/info").permitAll()
					.antMatchers("/management/**").hasRole("ADMIN")
					.anyRequest().authenticated()
					.and()
				.csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					.and()
				.logout()
					.logoutSuccessUrl(logoutRedirectUrl())
					.addLogoutHandler(revokeTokenLogoutHandler())
					.permitAll();
	}

	@Bean
	public LogoutProperties logoutProperties() {
		return new LogoutProperties();
	}

	@Data
	@ConfigurationProperties(prefix = "security.oauth2.logout")
	public class LogoutProperties {
		private String userLogoutUri;
		private String redirectUri;
		private String redirectParam;
		private String revokeTokenUri;
		private HttpMethod revokeTokenMethod;
		private String revokeTokenParam;
	}

	protected String logoutRedirectUrl() {
		if (StringUtils.isBlank(logoutProperties.getUserLogoutUri())) {
			return "/login";
		} else {
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(logoutProperties.getUserLogoutUri())
					.queryParam("client_id", resource.getClientId());
			if (StringUtils.isNotBlank(logoutProperties.getRedirectParam()) && StringUtils.isNotBlank(logoutProperties.getRedirectUri())) {
				builder.queryParam(logoutProperties.getRedirectParam(), logoutProperties.getRedirectUri());
			}
			return builder.toUriString();
		}
	}

	protected RevokeTokenLogoutHandler revokeTokenLogoutHandler() {
		RevokeTokenLogoutHandler handler = new RevokeTokenLogoutHandler();
		handler.setClientId(resource.getClientId());
		handler.setClientSecret(resource.getClientSecret());
		handler.setRevokeTokenUri(logoutProperties.getRevokeTokenUri());
		handler.setRevokeTokenMethod(logoutProperties.getRevokeTokenMethod());
		handler.setRevokeTokenParam(logoutProperties.getRevokeTokenParam());
		return handler;
	}

}
