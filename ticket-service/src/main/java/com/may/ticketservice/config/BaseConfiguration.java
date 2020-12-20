package com.may.ticketservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static java.util.function.Predicate.not;

@Configuration
@EnableJpaAuditing
@EnableElasticsearchRepositories
@ComponentScan("com.may")
public class BaseConfiguration {

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
