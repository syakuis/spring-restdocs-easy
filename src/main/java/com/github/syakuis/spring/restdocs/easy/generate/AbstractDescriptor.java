package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * Abstract class providing common functionality for generating RestDocs.
 * This includes resolving constraint descriptions and validation messages.
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
// todo rename AbstractConstraintDescriptions
abstract class AbstractDescriptor extends DescriptorMessageSourceAccessor {
    private final ConstraintDescriptions constraintDescriptions;

    /**
     * Constructor to initialize the AbstractRestDocs with message source and class type.
     *
     * @param messageSource The MessageSource for resolving validation messages
     * @param targetClass   The class for which descriptors are generated
     */
    AbstractDescriptor(MessageSource messageSource, Class<?> targetClass) {
        super(messageSource);
        this.constraintDescriptions = new ConstraintDescriptions(targetClass);
    }

    /**
     * Retrieves the validation constraints for a given field as an array of Attributes.
     *
     * @param fieldName The name of the field
     * @return An array of Attributes containing the field's validation constraints
     */
    protected Attributes.Attribute[] getConstraints(String fieldName) {
        return new Attributes.Attribute[]{
            key("constraints").value(String.join("\n\n", constraintDescriptions.descriptionsForProperty(fieldName)))
        };
    }
}
