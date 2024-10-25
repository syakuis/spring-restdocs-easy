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
 * Auto-configuration for "Spring REST Docs Easy".
 * Provides automatic configuration of REST Docs settings and integration with Spring Boot.
 *
 * <p>Features:</p>
 * - Configures URI scheme, host, port, and context path for documentation
 * - Integrates with Spring Boot's server properties
 * - Sets up pretty printing for request/response documentation
 * - Configures message source for i18n support
 *
 * <p>Configuration properties:</p>
 * - {@code spring.rest-docs-easy.uri-scheme}: URI scheme (defaults to REST Docs property)
 * - {@code spring.rest-docs-easy.uri-host}: Host name (defaults to REST Docs property)
 * - {@code spring.rest-docs-easy.uri-port}: Port number (defaults to REST Docs property)
 * - {@code spring.rest-docs-easy.uri-context-path-ignored}: Whether to ignore context path
 *
 * @author Seok Kyun. Choi.
 * @since 2021-08-14
 * @see RestDocsEasyProperties
 * @see org.springframework.boot.test.autoconfigure.restdocs.RestDocsProperties
 */
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties({RestDocsEasyProperties.class, RestDocsProperties.class, ServerProperties.class})
public class RestDocsEasyConfiguration {
    private final MessageSource messageSource;
    private final RestDocsEasyProperties restDocsEasyProperties;
    private final RestDocsProperties restDocsProperties;
    private final ServerProperties serverProperties;

    /**
     * Customizes Spring REST Docs MockMvc configuration.
     * Configures URI components and sets up pretty printing for documentation.
     *
     * <p>URI configuration priority:</p>
     * 1. REST Docs Easy properties
     * 2. Spring REST Docs properties
     * 3. Server properties (for context path)
     *
     * @return RestDocsMockMvcConfigurationCustomizer with configured URI settings
     */
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

    /**
     * Creates a RestDocs instance configured with message source support.
     * This bean is used for generating API documentation with internationalization support.
     *
     * @return configured RestDocs instance
     * @see com.github.syakuis.spring.restdocs.easy.generate.RestDocs
     */
    @Bean
    public RestDocs restDocs() {
        return RestDocs.builder().messageSource(messageSource).build();
    }
}

