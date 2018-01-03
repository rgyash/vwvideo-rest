package com.vwtest.video.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		AuthorizationScope[] authScopes = new AuthorizationScope[1];
		authScopes[0] = new AuthorizationScopeBuilder().scope("").build();

		SecurityReference securityReference = SecurityReference.builder().reference("basicAuth").scopes(authScopes)
				.build();

		ArrayList<SecurityReference> reference = new ArrayList<SecurityReference>(1);
		reference.add(securityReference);

		ArrayList<SecurityContext> securityContexts = new ArrayList<SecurityContext>(1);
		securityContexts.add(SecurityContext.builder().securityReferences(reference).build());

		List<SecurityScheme> schemeList = new ArrayList<>();
		schemeList.add(new BasicAuth("basicAuth"));
		return new Docket(DocumentationType.SWAGGER_2).securitySchemes(schemeList).securityContexts(securityContexts)
				.select().apis(RequestHandlerSelectors.basePackage("com.vwtest.video.controller"))
				.paths(regex("/rest.*")).build().apiInfo(metaInfo());
	}

	private ApiInfo metaInfo() {

		ApiInfo apiInfo = new ApiInfo("VWVideo Service", "Spring Boot Swagger VWVideo Service API", "1.0",
				"Terms of Service",
				new Contact("VWVideo", "https://www.VWVideo.com/VideoService", "VWVideo@VWVideo.com"),
				"Apache License Version 2.0", "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>());

		return apiInfo;
	}
}
