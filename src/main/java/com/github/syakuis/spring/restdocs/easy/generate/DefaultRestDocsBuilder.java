package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.function.Consumer;

/**
 * Default implementation of RestDocsBuilder for "Spring REST Docs Easy".
 * Provides builder pattern implementation for creating RestDocs instances
 * with customizable message sources and JSON field type mappings.
 *
 * <p>Features:</p>
 * - Message source configuration for i18n support
 * - Custom JSON field type mapping for Java types
 * - Fluent builder API with method chaining
 * - Default type mappings for common Java types
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RestDocs restDocs = new DefaultRestDocsBuilder()
 *     .messageSource(messageSource)
 *     .configure(mapper -> {
 *         // Custom type mappings
 *         mapper.add(LocalDate.class, JsonFieldType.STRING);
 *         mapper.add(ZonedDateTime.class, JsonFieldType.STRING);
 *         mapper.add(CustomEnum.class, JsonFieldType.STRING);
 *
 *         // Override existing mappings
 *         mapper.add(Long.class, JsonFieldType.NUMBER);
 *     })
 *     .build();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 *
 * @see JsonFieldTypeMapper
 * @see DefaultRestDocs
 */
class DefaultRestDocsBuilder implements RestDocsBuilder {
    private MessageSource messageSource;
    private final JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();;

    /**
     * Sets the message source for internationalization support.
     * The message source is used to resolve field descriptions and other text content.
     *
     * <p>Message resolution examples:</p>
     * - Field descriptions: "{user.email.description}"
     * - Enum value descriptions: "{user.status.active.description}"
     * - Constraint descriptions: "{validation.email.message}"
     *
     * @param messageSource Spring MessageSource for i18n support
     * @return this builder instance for method chaining
     */
    @Override
    public RestDocsBuilder messageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        return this;
    }

    /**
     * Configures the JSON field type mapper with custom type mappings.
     * Allows customization of how Java types are mapped to JSON field types
     * in the documentation. The mapper comes with default mappings for common
     * Java types, which can be overridden or extended.
     *
     * @param config Consumer function to configure JsonFieldTypeMapper
     * @return this builder instance for method chaining
     * @see JsonFieldTypeMapper#add(Class, JsonFieldType)
     */
    @Override
    public RestDocsBuilder configure(Consumer<JsonFieldTypeMapper> config) {
        config.accept(jsonFieldTypeMapper);
        return this;
    }

    /**
     * Builds and returns a new RestDocs instance with the configured settings.
     * Creates a new DefaultRestDocs instance using the configured message source
     * and JSON field type mapper.
     *
     * @return new RestDocs instance
     */
    @Override
    public RestDocs build() {
        return new DefaultRestDocs(messageSource, jsonFieldTypeMapper);
    }
}
