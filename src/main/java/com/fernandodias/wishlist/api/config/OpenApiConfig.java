package com.fernandodias.wishlist.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@ConditionalOnExpression(value = "${springdoc.api-docs.enabled:false}")
@EnableSpringDataWebSupport
@EnableWebMvc
public class OpenApiConfig extends WebMvcConfigurationSupport {

  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
                         .group("Wishlist APIs")
                         .pathsToMatch("/**")
                         .build();
  }

  @Bean
  OpenAPI wishlistOpenAPI() {
    return new OpenAPI().info(new Info().title(" Wishlist API")
                                        .description("Wishlist API's")
                                        .version("v1.0")
                                        .license(new License().name("Apache 2.0")
                                                              .url("http://springdoc.org")))
                        .externalDocs(new ExternalDocumentation().description("Wishlist API documentation")
                                                                 .url("https://wishlistApiDocumentations.xpto.com"));
  }
}