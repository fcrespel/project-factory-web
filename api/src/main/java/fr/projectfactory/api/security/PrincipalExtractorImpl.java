package fr.projectfactory.api.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalExtractorImpl implements PrincipalExtractor {

	private static final Logger logger = LoggerFactory.getLogger(PrincipalExtractorImpl.class);

	private final ExpressionParser expressionParser = new SpelExpressionParser();

	private String usernameAttribute;

	@Override
	public Object extractPrincipal(Map<String, Object> map) {
		// Get user name
		String username = getAttributeValue(map, usernameAttribute);
		if (username != null && !username.isEmpty()) {
			return username;
		}
		return null;
	}

	protected String getAttributeValue(Map<String, Object> map, String attributeName) {
		String attributeValue = null;
		if (attributeName != null && !attributeName.isEmpty()) {
			try {
				attributeValue = expressionParser.parseExpression(attributeName).getValue(map, String.class);
				if (attributeValue != null && !attributeValue.isEmpty()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Read attribute=" + attributeName + " from map=" + map);
					}
				}
			} catch (Exception e) {
				logger.error("Failed to get attribute=" + attributeName + " from map=" + map, e);
			}
		}
		return attributeValue;
	}

}
