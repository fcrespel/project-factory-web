package fr.projectfactory.manager.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.security.oauth2.sso.OAuth2SsoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
public class SecurityConfig extends OAuth2SsoConfigurerAdapter {

	public static final String XSRF_TOKEN_COOKIE = "XSRF-TOKEN";
	public static final String XSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
				.authorizeRequests()
					.antMatchers("/login").permitAll()
					.antMatchers("/**/*.css", "/**/*.js").permitAll()
					.anyRequest().authenticated()
					.and()
				.csrf()
					.csrfTokenRepository(csrfTokenRepository())
					.and()
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
	}

	protected static Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN_COOKIE);
					String token = csrf.getToken();
					if (cookie == null || (token != null && !token.equals(cookie.getValue()))) {
						cookie = new Cookie(XSRF_TOKEN_COOKIE, token);
						cookie.setPath(request.getContextPath());
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	protected static CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(XSRF_TOKEN_HEADER);
		return repository;
	}

}
