package com.may.accountservice.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.function.Predicate.not;

@Configuration
@EnableSwagger2
public class BaseConfigurations {
    private static final String EXCLUDED_PATHS = "(/actuator|/actuator/.*|/info|/health|/health/" +
            ".*|/metrics|/error|/env|/env/.*)";
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(not(PathSelectors.regex(EXCLUDED_PATHS)))
                .build();
    }


    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
