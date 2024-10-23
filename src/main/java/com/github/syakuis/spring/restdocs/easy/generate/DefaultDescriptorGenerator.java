package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation of DescriptorGenerator.
 * Manages a set of descriptors and provides methods to add and generate them.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 */
public class DefaultDescriptorGenerator implements DescriptorGenerator {
    private final Set<Descriptor> descriptors = new HashSet<>();

    /**
     * Validates that the field name is not null or blank.
     *
     * @param name The field name to validate
     * @throws IllegalArgumentException if the name is null or blank
     */
    private void validName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
    }

    /**
     * {@inheritDoc}
     * Creates a descriptor with default STRING type and not optional.
     */
    @Override
    public DescriptorGenerator add(String name, String description) {
        add(name, description, JsonFieldType.STRING, false);
        return this;
    }

    /**
     * {@inheritDoc}
     * Creates a descriptor with specified type and not optional.
     */
    @Override
    public DescriptorGenerator add(String name, String description, JsonFieldType type) {
        add(name, description, type, false);
        return this;
    }

    /**
     * {@inheritDoc}
     * Creates and adds a descriptor with complete specifications.
     */
    @Override
    public DescriptorGenerator add(String name, String description, JsonFieldType type, boolean optional) {
        validName(name);
        descriptors.add(Descriptor.builder()
            .name(name)
            .description(description)
            .type(type)
            .optional(optional).build());
        return this;
    }

    /**
     * {@inheritDoc}
     * Adds a pre-built descriptor to the set.
     */
    @Override
    public DescriptorGenerator add(Descriptor descriptor) {
        descriptors.add(descriptor);
        return this;
    }

    @Override
    public RestDocs.Operator generate() {
        return new DefaultRestDocs.DefaultOperator(descriptors.stream().toList());
    }
}
