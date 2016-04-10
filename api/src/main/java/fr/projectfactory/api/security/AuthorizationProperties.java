package fr.projectfactory.api.security;

import java.util.List;
import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationProperties {

	private String userAttribute;
	private String userAuthorityPrefix;
	private boolean userAuthorityUppercase;
	private String groupAttribute;
	private String groupAuthorityPrefix;
	private boolean groupAuthorityUppercase;
	private Map<String, List<String>> authorityMapping;

}
