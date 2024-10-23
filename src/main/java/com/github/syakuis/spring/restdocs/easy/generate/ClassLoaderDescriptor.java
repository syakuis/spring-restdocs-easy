package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.DataClassLoader;
import com.github.syakuis.spring.restdocs.easy.core.DataClassMetadata;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Collections;
import java.util.List;

/**
 * Generates RestDocs descriptors for a given class type.
 * This class loads field metadata, validates fields, and constructs descriptor objects.
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
public class ClassLoaderDescriptor extends AbstractDescriptor {
    private final Class<?> targetClass;

    /**
     * Constructor to initialize the RestDocs generator with a message source and the target class.
     *
     * @param messageSource MessageSource for resolving messages
     * @param targetClass   The class to generate descriptors for
     */
    public ClassLoaderDescriptor(MessageSource messageSource, Class<?> targetClass) {
        super(messageSource, targetClass);
        this.targetClass = targetClass;
    }

    /**
     * Generates a list of field descriptors for the target class.
     * Each descriptor includes field name, type, description, optionality, and constraints.
     *
     * @return A list of descriptors representing each field of the target class
     */
    public List<Descriptor> generate() {
        FieldOptionalValidator fieldOptionalValidator = new FieldOptionalValidator(Collections.emptyList());

        return loadFieldMetadata().stream().map(fieldMetadata -> {
                boolean hasConstraints = fieldOptionalValidator.hasValidationConstraint(fieldMetadata.field());

                return buildDescriptor(fieldMetadata, hasConstraints, fieldOptionalValidator);
            }
        ).toList();
    }

    /**
     * Loads the field metadata for the target class.
     *
     * @return A list of field metadata from the DataClassLoader
     */
    private List<DataClassMetadata> loadFieldMetadata() {
        return DataClassLoader.of(targetClass).toList();
    }

    /**
     * Builds a Descriptor object for a given field.
     *
     * @param fieldMetadata        Metadata of the field
     * @param hasConstraints       Whether the field has validation constraints
     * @param fieldOptionalValidator Validator to check if the field is optional
     * @return A Descriptor object representing the field
     */
    private Descriptor buildDescriptor(DataClassMetadata fieldMetadata, boolean hasConstraints, FieldOptionalValidator fieldOptionalValidator) {
        if (fieldMetadata.target().isEnum()) {
            return Descriptor.builder()
                .name(fieldMetadata.fieldName())
                .type(JsonFieldTypeMapper.get(fieldMetadata.fieldType()))
                .description(super.getMessage(fieldMetadata))
                .optional(false)
                .ignore(false)
                .attributes(new Attributes.Attribute[0])
                .build();
        }

        return Descriptor.builder()
            .name(fieldMetadata.fieldName())
            .type(JsonFieldTypeMapper.get(fieldMetadata.fieldType()))
            .description(super.getMessage(fieldMetadata))
            .optional(fieldOptionalValidator.isFieldOptional(fieldMetadata.field()))
            .ignore(false)
            .attributes(hasConstraints ? super.getConstraints(fieldMetadata.fieldName()) : new Attributes.Attribute[0])
            .build();
    }
}