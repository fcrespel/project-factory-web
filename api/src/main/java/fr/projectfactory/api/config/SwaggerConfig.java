package fr.projectfactory.api.config;

import java.security.Principal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket apiV1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("v1")
				.apiInfo(apiInfoV1())
				.ignoredParameterTypes(Principal.class)
				.select()
				.paths(PathSelectors.ant("/v1/**"))
				.build();
	}

	private ApiInfo apiInfoV1() {
		return new ApiInfoBuilder()
				.title("Platform API")
				.description("Platform management REST API. All operations are exposed as JSON and require an OAuth 2.0 Access Token granted by the Central Authentication Service.")
				.contact(new Contact("Project Factory team and contributors", "http://project-factory.fr", null))
				.version("1.0")
				.build();
	}

}
