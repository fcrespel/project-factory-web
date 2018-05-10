package fr.projectfactory.manager.security;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevokeTokenLogoutHandler implements LogoutHandler {

	private static final Logger logger = LoggerFactory.getLogger(RevokeTokenLogoutHandler.class);

	public static final String DEFAULT_REVOKE_TOKEN_PARAM = "token";

	private String clientId;
	private String clientSecret;
	private String revokeTokenUri;
	private HttpMethod revokeTokenMethod = HttpMethod.POST;
	private String revokeTokenParam = DEFAULT_REVOKE_TOKEN_PARAM;
	private RestOperations restTemplate = new RestTemplate();

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if (StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(clientSecret) && StringUtils.isNotBlank(revokeTokenUri) && revokeTokenMethod != null && StringUtils.isNotBlank(revokeTokenParam)) {
			try {
				Object details = authentication.getDetails();
				if (details instanceof OAuth2AuthenticationDetails) {
					String token = ((OAuth2AuthenticationDetails) details).getTokenValue();
					if (StringUtils.isNotBlank(token)) {
						HttpEntity<String> req = new HttpEntity<>(buildRevokeTokenHeaders());
						ResponseEntity<Void> resp = restTemplate.exchange(buildRevokeTokenUrl(token), revokeTokenMethod, req, Void.class);
						logger.debug("Token revoked with response code: {}", resp.getStatusCodeValue());
					}
				}
			} catch (Exception e) {
				logger.warn("Failed to revoke token during logout", e);
			}
		}
	}

	protected HttpHeaders buildRevokeTokenHeaders() throws UnsupportedEncodingException {
		HttpHeaders headers = new HttpHeaders();
		String creds = String.format("%s:%s", clientId, clientSecret);
		String authorization = "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
		headers.add("Authorization", authorization);
		return headers;
	}

	protected String buildRevokeTokenUrl(String token) {
		return UriComponentsBuilder
				.fromUriString(revokeTokenUri)
				.queryParam(revokeTokenParam, token)
				.toUriString();
	}

}
