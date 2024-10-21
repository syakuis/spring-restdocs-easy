package com.github.syakuis.spring.restdocs.easy.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsProperties;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-14
 */
@Slf4j
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties({RestDocsExtendProperties.class, RestDocsProperties.class, ServerProperties.class})
public class RestDocsGenerationConfiguration {
    private final RestDocsProperties restDocsProperties;
    private final RestDocsExtendProperties restDocsExtendProperties;
    private final ServerProperties serverProperties;

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        var uriScheme = Objects.requireNonNullElse(restDocsExtendProperties.uriScheme(), restDocsProperties.getUriScheme());
        var uriHost = Objects.requireNonNullElse(restDocsExtendProperties.uriHost(), restDocsProperties.getUriHost());
        var uriContextPath = restDocsExtendProperties.uriContextPathIgnored() ? "" :
            Objects.requireNonNullElse(serverProperties.getServlet().getContextPath(), "");
        var uriPort = Objects.requireNonNullElse(restDocsExtendProperties.uriPort(), restDocsProperties.getUriPort());

        restDocsProperties.setUriScheme(uriScheme);
        restDocsProperties.setUriHost(uriHost + uriContextPath);
        restDocsProperties.setUriPort(uriPort);

        return configurer -> configurer
            .operationPreprocessors()
            .withRequestDefaults(prettyPrint())
            .withResponseDefaults(prettyPrint());
    }
}

