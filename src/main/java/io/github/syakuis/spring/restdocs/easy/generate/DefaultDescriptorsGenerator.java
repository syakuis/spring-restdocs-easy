package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation of DescriptorsGenerator for "Spring REST Docs Easy".
 * Manages field descriptors with support for message internationalization
 * and Spring REST Docs integration. This implementation extends Spring REST Docs
 * to provide enhanced field documentation capabilities.
 *
 * <p>Key features:</p>
 * - Maintains unique descriptors using HashSet
 * - Supports message source integration for i18n
 * - Validates field names
 * - Provides prefix support for nested structures
 * - Generates Spring REST Docs compatible descriptors
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * DefaultDescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);
 * generator
 *     .add("id", "{user.id.description}", JsonFieldType.NUMBER)
 *     .add("email", "{user.email.description}", JsonFieldType.STRING, true)
 *     .add("role", "User role");
 *
 * // Generate with prefix for nested structure
 * RestDocs.Operator operator = generator.generate("user");
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 */
// todo descriptor cache
public class DefaultDescriptorsGenerator extends DescriptionMessageSource implements DescriptorsGenerator {
    private final Set<Descriptor> descriptors = new HashSet<>();

    public DefaultDescriptorsGenerator(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Validates that the field name is not null or blank.
     *
     * @param name the field name to validate
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
    public DescriptorsGenerator add(String name, String description) {
        add(name, description, JsonFieldType.STRING, false);
        return this;
    }

    /**
     * {@inheritDoc}
     * Creates a descriptor with specified type and not optional.
     */
    @Override
    public DescriptorsGenerator add(String name, String description, JsonFieldType type) {
        add(name, description, type, false);
        return this;
    }

    /**
     * {@inheritDoc}
     * Creates and adds a descriptor with complete specifications.
     * The description can be a direct string or a message key in the format {key}.
     */
    @Override
    public DescriptorsGenerator add(String name, String description, JsonFieldType type, boolean optional) {
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
     * If a descriptor with the same name already exists, it will be replaced
     * due to HashSet behavior.
     */
    @Override
    public DescriptorsGenerator add(Descriptor descriptor) {
        descriptors.add(descriptor);
        return this;
    }

    /**
     * Generates an operator for creating documentation snippets.
     * Equivalent to calling {@code generate(null)}.
     *
     * @return operator configured with the current descriptors
     */
    @Override
    public RestDocs.Operator generate() {
        return generate(null);
    }

    /**
     * Generates an operator for creating documentation snippets with a prefix.
     * Resolves message keys in descriptions and applies the specified prefix
     * to all field paths.
     *
     * @param prefix optional prefix for field paths (e.g., "user" for "user.name")
     * @return operator configured with the current descriptors and prefix
     */
    @Override
    public RestDocs.Operator generate(String prefix) {
        return new DefaultRestDocs.DefaultOperator(descriptors.stream().map(it -> it.description(getMessageByExpression(it.description()))).toList());
    }
}
