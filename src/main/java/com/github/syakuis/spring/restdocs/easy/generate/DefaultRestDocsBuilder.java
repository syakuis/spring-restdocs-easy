package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;

import java.util.function.Consumer;

/**
 * Builder class for constructing RestDocs instances.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */
class DefaultRestDocsBuilder implements RestDocsBuilder {
    private MessageSource messageSource;
    private final JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();;

    @Override
    public RestDocsBuilder messageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        return this;
    }

    @Override
    public RestDocsBuilder configure(Consumer<JsonFieldTypeMapper> config) {
        config.accept(jsonFieldTypeMapper);
        return this;
    }

    @Override
    public RestDocs build() {
        return new DefaultRestDocs(messageSource, jsonFieldTypeMapper);
    }
}
