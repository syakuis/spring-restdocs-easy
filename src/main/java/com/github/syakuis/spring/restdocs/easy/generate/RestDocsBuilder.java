package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;

import java.util.function.Consumer;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */
public interface RestDocsBuilder {
    RestDocsBuilder messageSource(MessageSource messageSource);
    RestDocsBuilder configure(Consumer<JsonFieldTypeMapper> config);
    RestDocs build();
}
