package fr.projectfactory.api.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalExtractorImpl extends AbstractAttributeExtractor implements PrincipalExtractor {

	private static final Logger logger = LoggerFactory.getLogger(PrincipalExtractorImpl.class);
	
	private String usernameAttribute;

	@Override
	public Object extractPrincipal(Map<String, Object> map) {
		// Get user name
		String username = getAttributeValue(map, usernameAttribute);
		logger.debug("Extracted principal={}", username);
		if (username != null && !username.isEmpty()) {
			return username;
		}
		return null;
	}

}
