package com.barco.common.config;

import com.google.common.base.Predicates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.*;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Logger logger = LogManager.getLogger(SwaggerConfig.class);

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
            .securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey()))
            .select().apis(RequestHandlerSelectors.any()).paths(Predicates.not(PathSelectors.regex("/error")))
            .paths(PathSelectors.any()).build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfo("Barco Cron API", "Barco Cron is scheduler base api which help to run your process on time.",
                "1.0", "Terms of service",
            new Contact("Nabeel Ahmed", "www.barco.cron.com", "nabeel.amd93@gmail.com"),
                "License of API", "API license URL",
            Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
