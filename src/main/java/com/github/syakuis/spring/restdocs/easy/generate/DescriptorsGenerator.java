package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;

/**
 * Interface for generating descriptors that will be passed to Spring REST Docs.
 * Provides methods to create and manage descriptors with names, descriptions, types, and optional flags.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 * @see org.springframework.restdocs.payload.PayloadDocumentation
 */
public interface DescriptorsGenerator {
    /**
     * Creates a descriptor with default STRING type and not optional.
     *
     * @param name The name of the field to be documented
     * @param description The description of the field
     * @return This generator instance for method chaining
     * @throws IllegalArgumentException if name is null or blank
     */
    DescriptorsGenerator add(String name, String description);

    /**
     * Creates a descriptor with specified type and not optional.
     *
     * @param name The name of the field to be documented
     * @param description The description of the field
     * @param type The field type for the documentation
     * @return This generator instance for method chaining
     * @throws IllegalArgumentException if name is null or blank
     */
    DescriptorsGenerator add(String name, String description, JsonFieldType type);

    /**
     * Creates a descriptor with complete specifications.
     *
     * @param name The name of the field to be documented
     * @param description The description of the field
     * @param type The field type for the documentation
     * @param optional Whether the field is optional
     * @return This generator instance for method chaining
     * @throws IllegalArgumentException if name is null or blank
     */
    DescriptorsGenerator add(String name, String description, JsonFieldType type, boolean optional);

    /**
     * Adds a pre-built descriptor.
     *
     * @param descriptor The pre-built descriptor to add
     * @return This generator instance for method chaining
     */
    DescriptorsGenerator add(Descriptor descriptor);

    /**
     * Generates an operator containing all added descriptors.
     *
     * @return An operator containing the list of descriptors
     */
    RestDocs.Operator generate();
}
