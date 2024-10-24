package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.ClassFieldMetadata;
import com.github.syakuis.spring.restdocs.easy.core.ClassMetadataGenerator;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.List;

/**
 * Generates RestDocs descriptors for a given class type.
 * This class loads field metadata, validates fields, and constructs descriptor objects.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
public class ClassDescriptorGenerator extends DescriptionMessageSource {
    private final JsonFieldTypeMapper jsonFieldTypeMapper;

    /**
     * Constructor to initialize the RestDocs generator with a message source and the target class.
     *
     * @param messageSource MessageSource for resolving messages
     */
    public ClassDescriptorGenerator(MessageSource messageSource, JsonFieldTypeMapper jsonFieldTypeMapper) {
        super(messageSource);
        this.jsonFieldTypeMapper = jsonFieldTypeMapper;
    }

    public List<Descriptor> generate(Class<?> targetClass, Class<?>... validGroups) {
        return generate(null, targetClass, validGroups);
    }

    /**
     * Generates a list of field descriptors for the target class.
     * Each descriptor includes field name, type, description, optionality, and constraints.
     *
     * @return A list of descriptors representing each field of the target class
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
     * Builds a Descriptor object for a given field.
     *
     * @param fieldMetadata        Metadata of the field
     * @param hasConstraints       Whether the field has validation constraints
     * @param fieldOptionalValidator Validator to check if the field is optional
     * @return A Descriptor object representing the field
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