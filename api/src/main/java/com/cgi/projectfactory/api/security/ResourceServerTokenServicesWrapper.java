package com.cgi.projectfactory.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Data
public class ResourceServerTokenServicesWrapper implements ResourceServerTokenServices {

	private static final Log logger = LogFactory.getLog(ResourceServerTokenServicesWrapper.class);

	private final ExpressionParser expressionParser = new SpelExpressionParser();

	private ResourceServerTokenServices resourceServerTokenServices;
	private String userAttribute;
	private GrantedAuthoritiesMapper userAuthorityMapper;
	private String groupAttribute;
	private GrantedAuthoritiesMapper groupAuthorityMapper;
	private Map<String, List<String>> authorityMapping;

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
		OAuth2Authentication auth = resourceServerTokenServices.loadAuthentication(accessToken);
		Authentication userAuth = auth.getUserAuthentication();

		UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userAuth.getPrincipal(), userAuth.getCredentials(), getGrantedAuthorities(userAuth));
		user.setDetails(userAuth.getDetails());

		return new OAuth2Authentication(auth.getOAuth2Request(), user);
	}

	protected Set<GrantedAuthority> getGrantedAuthorities(Authentication userAuth) {
		// Copy existing authorities
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(userAuth.getAuthorities());

		// Add user authority
		if (userAttribute != null) {
			try {
				String user = expressionParser.parseExpression(userAttribute).getValue(userAuth, String.class);
				if (user != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Read user attribute=" + user + " from authentication=" + userAuth);
					}
					Collection<? extends GrantedAuthority> userAuthorities = AuthorityUtils.createAuthorityList(user);
					if (getUserAuthorityMapper() != null) {
						userAuthorities = getUserAuthorityMapper().mapAuthorities(userAuthorities);
					}
					authorities.addAll(userAuthorities);
				}
			} catch (Exception e) {
				logger.error("Failed to get user attribute from authentication=" + userAuth, e);
			}
		}

		// Add group authorities
		if (groupAttribute != null) {
			try {
				String groups = expressionParser.parseExpression(groupAttribute).getValue(userAuth, String.class);
				if (groups != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Read group attribute=" + groups + " from authentication=" + userAuth);
					}
					Collection<? extends GrantedAuthority> groupAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(groups);
					if (getGroupAuthorityMapper() != null) {
						groupAuthorities = getGroupAuthorityMapper().mapAuthorities(groupAuthorities);
					}
					authorities.addAll(groupAuthorities);
				}
			} catch (Exception e) {
				logger.error("Failed to get group attribute from authentication=" + userAuth, e);
			}
		}

		// Add mapped authorities
		if (authorityMapping != null) {
			Set<String> currentAuthorities = AuthorityUtils.authorityListToSet(authorities);
			for (Entry<String, List<String>> mapping : authorityMapping.entrySet()) {
				for (String authority : mapping.getValue()) {
					if (currentAuthorities.contains(authority)) {
						authorities.add(new SimpleGrantedAuthority(mapping.getKey()));
						break;
					}
				}
			}
		}

		return authorities;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		return resourceServerTokenServices.readAccessToken(accessToken);
	}

}
