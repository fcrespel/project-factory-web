package fr.projectfactory.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathBeanPostProcessor;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

	@Bean
	@ConfigurationProperties(prefix = "ldap.contextSource")
	public LdapContextSource contextSource() {
		return new LdapContextSource();
	}

	@Bean
	public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
		return new LdapTemplate(contextSource);
	}
	
	@Bean
	public BaseLdapPathBeanPostProcessor baseLdapPathBeanPostProcessor() {
		return new BaseLdapPathBeanPostProcessor();
	}

}
