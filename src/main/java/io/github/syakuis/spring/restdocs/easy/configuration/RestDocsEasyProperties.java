package io.github.syakuis.spring.restdocs.easy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for "Spring REST Docs Easy".
 * Provides customization options for REST API documentation URI components.
 *
 * <p>Properties prefix: {@code spring.test.restdocs-easy}</p>
 *
 * <p>Available properties:</p>
 * <ul>
 *   <li>{@code uri-scheme}: The URI scheme (e.g., "http", "https")</li>
 *   <li>{@code uri-host}: The host name (e.g., "api.example.com")</li>
 *   <li>{@code uri-port}: The port number (e.g., 8080)</li>
 *   <li>{@code uri-context-path-ignored}: Whether to ignore the application's context path in documentation</li>
 * </ul>
 *
 * <p>Example configuration:</p>
 * <pre>{@code
 * spring:
 *   test:
 *     restdocs-easy:
 *       uri-scheme: https
 *       uri-host: api.example.com
 *       uri-port: 443
 *       uri-context-path-ignored: false
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */
@ConfigurationProperties("spring.test.restdocs-easy")
public record RestDocsEasyProperties(

    /**
     * The URI scheme to use in the documentation.
     * If not specified, falls back to Spring REST Docs default.
     */
    String uriScheme,

    /**
     * The URI host to use in the documentation.
     * If not specified, falls back to Spring REST Docs default.
     */
    String uriHost,

    /**
     * The URI port to use in the documentation.
     * If not specified, falls back to Spring REST Docs default.
     */
    Integer uriPort,

    /**
     * Whether to ignore the application's context path in the documented URIs.
     * Default is false, meaning the context path will be included.
     */
    boolean uriContextPathIgnored
) {
}
