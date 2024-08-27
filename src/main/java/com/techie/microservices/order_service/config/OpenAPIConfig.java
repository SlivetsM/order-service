package com.techie.microservices.order_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI orderServiceApi() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("Order Service API")
                        .description("This is REST API for Order Service. It provides CRUD operations for Order. ")
                        .version("v0.0.1")
                        .license(new io.swagger.v3.oas.models.info.License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation().description("Other Documentation").url("https://order-service-dummy-url.org/docs"));
    }
}

