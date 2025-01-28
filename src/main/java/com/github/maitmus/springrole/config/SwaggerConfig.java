package com.github.maitmus.springrole.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Role API")
                        .version("0.1")
                        .description("Spring Role API Documentation")
                        .license(new License().name("MIT License")
                                .url("https://github.com/maitmus/springrole"))
                );
    }
}
