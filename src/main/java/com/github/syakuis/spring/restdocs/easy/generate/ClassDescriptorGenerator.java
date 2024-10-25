package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.ClassFieldMetadata;
import com.github.syakuis.spring.restdocs.easy.core.ClassMetadataGenerator;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.List;

/**
 * Core descriptor generator for "Spring REST Docs Easy" that creates documentation
 * descriptors by analyzing class fields, validation constraints, and messages.
 *
 * <p>Key features:</p>
 * - Generates field descriptors with type information
 * - Special handling for enum fields and their constants
 * - Integrates validation constraints into documentation
 * - Supports validation groups for conditional validation
 * - Provides i18n support through message source
 * - Handles nested structures with prefix support
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ClassDescriptorGenerator generator = new ClassDescriptorGenerator(messageSource, typeMapper);
 *
 * // Generate descriptors for a simple class
 * List<Descriptor> descriptors = generator.generate(UserDto.class);
 *
 * // Generate descriptors with validation groups
 * List<Descriptor> validatedDescriptors = generator.generate(
 *     UserDto.class,
 *     CreateGroup.class
 * );
 *
 * // Generate descriptors for nested structure
 * List<Descriptor> nestedDescriptors = generator.generate(
 *     "user",
 *     UserDto.class
 * );
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
public class ClassDescriptorGenerator extends DescriptionMessageSource {
    private final JsonFieldTypeMapper jsonFieldTypeMapper;

    /**
     * Creates a new descriptor generator for "Spring REST Docs Easy".
     *
     * @param messageSource source for resolving i18n messages (e.g., "{user.email.description}")
     * @param jsonFieldTypeMapper mapper for converting Java types to Spring REST Docs JsonFieldType
     */
    public ClassDescriptorGenerator(MessageSource messageSource, JsonFieldTypeMapper jsonFieldTypeMapper) {
        super(messageSource);
        this.jsonFieldTypeMapper = jsonFieldTypeMapper;
    }

    /**
     * Generates descriptors for a class without prefix.
     * Equivalent to calling {@code generate(null, targetClass, validGroups)}.
     *
     * @param targetClass the class to generate descriptors for
     * @param validGroups optional validation groups to consider
     * @return list of descriptors for documentation
     */
    public List<Descriptor> generate(Class<?> targetClass, Class<?>... validGroups) {
        return generate(null, targetClass, validGroups);
    }

    /**
     * Generates descriptors for a class with optional prefix and validation groups.
     *
     * <p>Examples of prefix usage:</p>
     * - "user" → fields become "user.name", "user.email"
     * - "users[]" → fields become "users[].id", "users[].name"
     * - "user.address" → fields become "user.address.street", "user.address.city"
     *
     * @param prefix optional prefix for nested structures (can be null)
     * @param targetClass the class to generate descriptors for
     * @param validGroups optional validation groups to consider
     * @return list of descriptors for documentation
     */
    public List<Descriptor> generate(String prefix, Class<?> targetClass, Class<?>... validGroups) {
        ClassFieldConstraintDescriptions constraintDescriptions = new ClassFieldConstraintDescriptions(targetClass);
        FieldOptionalValidator fieldOptionalValidator = new FieldOptionalValidator(Arrays.stream(validGroups).toList());

        return ClassMetadataGenerator.of(targetClass).toList().stream().map(fieldMetadata -> {
                boolean hasConstraints = fieldOptionalValidator.hasValidationConstraint(fieldMetadata.field());

                return buildDescriptor(prefix, fieldMetadata, hasConstraints, fieldOptionalValidator, constraintDescriptions);
            }
        ).toList();
    }

    /**
     * Builds a single field descriptor with complete metadata.
     * Handles both enum and regular fields differently, applying appropriate
     * constraints and optional status.
     *
     * @param prefix Prefix for the field path
     * @param fieldMetadata Metadata about the field
     * @param hasConstraints Whether the field has validation constraints
     * @param fieldOptionalValidator Validator for determining field optionality
     * @param constraintDescriptions Provider of constraint descriptions
     * @return A complete field descriptor
     */
    private Descriptor buildDescriptor(String prefix,
                                       ClassFieldMetadata fieldMetadata,
                                       boolean hasConstraints,
                                       FieldOptionalValidator fieldOptionalValidator,
                                       ClassFieldConstraintDescriptions constraintDescriptions) {
        if (fieldMetadata.target().isEnum()) {
            return Descriptor.builder()
                .prefix(prefix)
                .name(fieldMetadata.name())
                .type(jsonFieldTypeMapper.get(fieldMetadata.type()))
                .description(super.getMessage(fieldMetadata))
                .optional(false)
                .ignore(false)
                .attributes(new Attributes.Attribute[0])
                .build();
        }

        return Descriptor.builder()
            .prefix(prefix)
            .name(fieldMetadata.name())
            .type(jsonFieldTypeMapper.get(fieldMetadata.type()))
            .description(super.getMessage(fieldMetadata))
            .optional(fieldOptionalValidator.isFieldOptional(fieldMetadata.field()))
            .ignore(false)
            .attributes(hasConstraints ? constraintDescriptions.getConstraints(fieldMetadata.name()) : new Attributes.Attribute[0])
            .build();
    }
}