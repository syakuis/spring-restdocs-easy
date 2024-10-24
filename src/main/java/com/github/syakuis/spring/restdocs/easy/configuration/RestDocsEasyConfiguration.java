package com.github.syakuis.spring.restdocs.easy.configuration;

import com.github.syakuis.spring.restdocs.easy.generate.RestDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-14
 */
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties({RestDocsEasyProperties.class, RestDocsProperties.class, ServerProperties.class})
public class RestDocsEasyConfiguration {
    private final MessageSource messageSource;
    private final RestDocsEasyProperties restDocsEasyProperties;
    private final RestDocsProperties restDocsProperties;
    private final ServerProperties serverProperties;

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        var uriScheme = Objects.requireNonNullElse(restDocsEasyProperties.uriScheme(), restDocsProperties.getUriScheme());
        var uriHost = Objects.requireNonNullElse(restDocsEasyProperties.uriHost(), restDocsProperties.getUriHost());
        var uriContextPath = restDocsEasyProperties.uriContextPathIgnored() ? "" :
            Objects.requireNonNullElse(serverProperties.getServlet().getContextPath(), "");
        var uriPort = Objects.requireNonNullElse(restDocsEasyProperties.uriPort(), restDocsProperties.getUriPort());

        restDocsProperties.setUriScheme(uriScheme);
        restDocsProperties.setUriHost(uriHost + uriContextPath);
        restDocsProperties.setUriPort(uriPort);

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

