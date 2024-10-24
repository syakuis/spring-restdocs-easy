package com.github.syakuis.spring.restdocs.easy.configuration;

import com.github.syakuis.spring.restdocs.easy.generate.RestDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-14
 */
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties
public class RestDocsEasyConfiguration {
    private final MessageSource messageSource;

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer
            .operationPreprocessors()
            .withRequestDefaults(prettyPrint())
            .withResponseDefaults(prettyPrint());
    }

    @Bean
    public RestDocs restDocs() {
        return RestDocs.builder().messageSource(messageSource).build();
    }
}

