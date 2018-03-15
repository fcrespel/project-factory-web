package fr.projectfactory.manager.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

}
