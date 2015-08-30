package com.cgi.projectfactory.api.config;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import com.google.common.base.Predicate;

@Configuration
public class SwaggerConfig {

	@Autowired
	private AuthorizationCodeResourceDetails oauthClient;

	@Bean
	public Docket apiV1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("v1")
				.apiInfo(apiInfoV1())
				.ignoredParameterTypes(Principal.class)
				.select()
				.paths(apiPathsV1())
				.build();
	}

	private ApiInfo apiInfoV1() {
		return new ApiInfoBuilder()
				.title("Platform API")
				.description("Platform management REST API. All operations are exposed as JSON and require an OAuth 2.0 Access Token granted by the Central Authentication Service.")
				.contact("iT-Toolbox (contact@it-toolbox.fr)")
				.version("1.0")
				.build();
	}

	private Predicate<String> apiPathsV1() {
		return new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.startsWith("/v1/");
			}
		};
	}

}
