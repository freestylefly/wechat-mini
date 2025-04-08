package com.bazi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API文档配置
 * 
 * @author bazi-team
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI baziOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("八字命理小助手 API")
                        .description("八字命理小助手后端API文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Bazi Team")
                                .email("support@bazi.com")
                                .url("https://bazi.com"))
                        .license(new License()
                                .name("私有软件")
                                .url("https://bazi.com/license")));
    }
} 