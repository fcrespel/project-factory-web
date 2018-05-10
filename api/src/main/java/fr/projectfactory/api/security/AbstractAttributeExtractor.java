package fr.projectfactory.api.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public abstract class AbstractAttributeExtractor {

	private static final Logger logger = LoggerFactory.getLogger(AbstractAttributeExtractor.class);

	private final ExpressionParser expressionParser = new SpelExpressionParser();

	protected String getAttributeValue(Map<String, Object> map, String attributeName) {
		String attributeValue = null;
		if (attributeName != null && !attributeName.isEmpty()) {
			try {
				attributeValue = expressionParser.parseExpression(attributeName).getValue(map, String.class);
				if (attributeValue != null && !attributeValue.isEmpty()) {
					logger.debug("Read attribute={} from map={}, value={}", attributeName, map, attributeValue);
				} else {
					logger.debug("Missing attribute={} in map={}", attributeName, map);
				}
			} catch (Exception e) {
				logger.error("Failed to get attribute={} from map={}", attributeName, map, e);
			}
		}
		return attributeValue;
	}

}
