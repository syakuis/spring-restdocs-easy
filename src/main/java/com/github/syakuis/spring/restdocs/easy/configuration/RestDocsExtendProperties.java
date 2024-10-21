package com.github.syakuis.spring.restdocs.easy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-03-08
 */
@ConfigurationProperties("spring.restdocs")
public record RestDocsExtendProperties(
    String uriScheme,
    String uriHost,
    Integer uriPort,
    boolean uriContextPathIgnored
) {
}
