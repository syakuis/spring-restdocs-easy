package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;

/**
 * Interface for generating field descriptors in "Spring REST Docs Easy".
 * Provides methods to create and manage descriptors for API documentation with support
 * for field names, descriptions, types, and optional flags. This interface is part of
 * "Spring REST Docs Easy", which extends Spring REST Docs functionality.
 *
 * <p>Features:</p>
 * - Flexible descriptor creation with various configurations
 * - Support for different field types using Spring REST Docs JsonFieldType
 * - Optional/required field specification
 * - Prefix support for nested structures
 * - Message source integration for i18n
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource)
 *     .add("id", "{user.id.description}", JsonFieldType.NUMBER)
 *     .add("email", "{user.email.description}", JsonFieldType.STRING, true);
 *
 * RestDocs.Operator operator = generator.generate("user");
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 * @see org.springframework.restdocs.payload.PayloadDocumentation
 */
public interface DescriptorsGenerator {
    /**
     * Creates a descriptor with default STRING type and not optional.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param name the name of the field to be documented (e.g., "email", "user.name")
     * @param description the description or message key (e.g., "User email", "{user.email.description}")
     * @return this generator instance for method chaining
     */
    DescriptorsGenerator add(String name, String description);

    /**
     * Creates a descriptor with specified type and not optional.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param name the name of the field to be documented (e.g., "age", "user.age")
     * @param description the description or message key (e.g., "User age", "{user.age.description}")
     * @param type the JSON field type (e.g., NUMBER for integers, STRING for text)
     * @return this generator instance for method chaining
     */
    DescriptorsGenerator add(String name, String description, JsonFieldType type);

    /**
     * Creates a descriptor with complete specifications.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param name the name of the field to be documented (e.g., "email", "user.email")
     * @param description the description or message key (e.g., "User email", "{user.email.description}")
     * @param type the JSON field type (e.g., STRING, NUMBER, BOOLEAN)
     * @param optional true if the field is optional, false if required
     * @return this generator instance for method chaining
     */
    DescriptorsGenerator add(String name, String description, JsonFieldType type, boolean optional);

    /**
     * Adds a pre-built descriptor to the generator.
     * Useful when you have a descriptor instance with custom configurations.
     *
     * @param descriptor the pre-configured descriptor to add
     * @return this generator instance for method chaining
     */
    DescriptorsGenerator add(Descriptor descriptor);

    /**
     * Generates an operator containing all added descriptors.
     * Equivalent to calling {@code generate(null)}.
     *
     * @return an operator containing the list of descriptors
     */
    RestDocs.Operator generate();

    /**
     * Generates an operator containing all added descriptors with a prefix.
     * The prefix is added to all field paths, useful for nested structures.
     *
     * <p>Example prefixes:</p>
     * - "user." for nested object fields (e.g., "user.name", "user.email")
     * - "addresses[]." for array fields (e.g., "addresses[].street", "addresses[].city")
     *
     * @param prefix the prefix to add to all field paths (can be null)
     * @return an operator containing the list of descriptors with prefixed paths
     */
    RestDocs.Operator generate(String prefix);
}
