package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;

import java.util.function.Consumer;

/**
 * Builder interface for creating RestDocs instances in "Spring REST Docs Easy".
 * Provides a fluent API to configure message sources and JSON field type mappings
 * for REST API documentation.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RestDocs restDocs = RestDocs.builder()
 *     .messageSource(messageSource)
 *     .configure(mapper -> {
 *         mapper.add(CustomType.class, JsonFieldType.STRING);
 *     })
 *     .build();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */
public interface RestDocsBuilder {

    /**
     * Sets the message source for internationalization support.
     * The message source is used to resolve field descriptions and other text content.
     *
     * <p>Message key format:</p>
     * - Field descriptions: "{user.email.description}"
     * - Enum descriptions: "{user.status.description}"
     * - Parameter descriptions: "{api.param.userId.description}"
     *
     * @param messageSource Spring MessageSource for i18n
     * @return this builder instance
     */
    RestDocsBuilder messageSource(MessageSource messageSource);

    /**
     * Configures custom JSON field type mappings.
     * Allows customization of how Java types are mapped to JSON field types
     * in the documentation.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * builder.configure(mapper -> {
     *     // Custom type mappings
     *     mapper.add(LocalDate.class, JsonFieldType.STRING);
     *     mapper.add(YourEnum.class, JsonFieldType.STRING);
     *
     *     // Override existing mappings
     *     mapper.add(BigDecimal.class, JsonFieldType.NUMBER);
     * });
     * }</pre>
     *
     * @param config Consumer function to configure JsonFieldTypeMapper
     * @return this builder instance
     */
    RestDocsBuilder configure(Consumer<JsonFieldTypeMapper> config);

    /**
     * Builds and returns a configured RestDocs instance.
     *
     * @return configured RestDocs instance
     */
    RestDocs build();
}
