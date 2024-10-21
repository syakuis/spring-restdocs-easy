package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.restdocs.constraints.*;

import java.util.ResourceBundle;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
abstract class AbstractRestDocs extends RestDocsMessageSourceAccessor {
    private final ConstraintResolver constraintResolver = new ValidatorConstraintResolver();
    private final ConstraintDescriptionResolver constraintDescriptionResolver = new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("DefaultConstraintDescriptions"));

    AbstractRestDocs(MessageSource messageSource) {
        super(messageSource);
    }

    protected ConstraintDescriptions getConstraintDescriptions(Class<?> objectType) {
        return new ConstraintDescriptions(objectType, constraintResolver, constraintDescriptionResolver);
    }
}
