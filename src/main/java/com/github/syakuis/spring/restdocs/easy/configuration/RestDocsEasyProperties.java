package com.github.syakuis.spring.restdocs.easy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */
@ConfigurationProperties("spring.test.restdocs-easy")
public record RestDocsEasyProperties(
    String uriScheme,
    String uriHost,
    Integer uriPort,
    boolean uriContextPathIgnored
) {
}
