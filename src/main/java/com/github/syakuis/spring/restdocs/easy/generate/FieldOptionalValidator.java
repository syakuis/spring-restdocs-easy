package com.github.syakuis.spring.restdocs.easy.generate;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This class is responsible for validating whether a field is optional or mandatory
 * based on validation annotations such as {@code @NotNull}, {@code @NotEmpty}, and {@code @NotBlank}.
 * It also handles validation groups to determine if the field should be validated in specific contexts.
 *
 * @author Seok Kyun. Choi.
 * @since 2023-07-14
 */
public class FieldOptionalValidator {

    /**
     * List of validation groups to be considered during field validation.
     * This list is injected via constructor and cannot be modified.
     */
    private final List<Class<?>> validationGroups;

    /**
     * List of validation annotations that indicate a field is mandatory.
     * This can be extended dynamically if necessary.
     */
    private final List<Class<? extends Annotation>> mandatoryAnnotations = List.of(NotNull.class, NotEmpty.class, NotBlank.class);

    /**
     * Constructor that takes validation groups as a parameter.
     *
     * @param validationGroups The validation groups to be considered during field validation.
     */
    public FieldOptionalValidator(List<Class<?>> validationGroups) {
        this.validationGroups = validationGroups;
    }

    /**
     * Checks if the given annotation is a mandatory validation annotation
     * (e.g., {@code @NotNull}, {@code @NotEmpty}, {@code @NotBlank}).
     *
     * @param annotation The annotation to check.
     * @return {@code true} if the annotation indicates a mandatory field, otherwise {@code false}.
     */
    private boolean isMandatoryAnnotation(Annotation annotation) {
        return mandatoryAnnotations.contains(annotation.annotationType());
    }

    /**
     * Checks if the given annotation has no groups or empty groups, meaning it applies to all groups.
     *
     * @param annotation The annotation to check.
     * @return {@code true} if the annotation's groups are empty or null, otherwise {@code false}.
     */
    private boolean hasNoGroups(Annotation annotation) {
        try {
            Method groupsMethod = annotation.annotationType().getDeclaredMethod("groups");
            Class<?>[] annotationGroups = (Class<?>[]) groupsMethod.invoke(annotation);
            return annotationGroups == null || annotationGroups.length == 0;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return false; // Safe fallback for annotations without "groups"
        }
    }

    /**
     * Checks if the given annotation has any groups matching the provided validation groups.
     *
     * @param annotation The annotation to check.
     * @return {@code true} if the annotation has matching groups, otherwise {@code false}.
     */
    private boolean matchesAnyGroup(Annotation annotation) {
        try {
            Method groupsMethod = annotation.annotationType().getDeclaredMethod("groups");
            Class<?>[] annotationGroups = (Class<?>[]) groupsMethod.invoke(annotation);
            for (Class<?> annotationGroup : annotationGroups) {
                if (validationGroups.contains(annotationGroup)) {
                    return true;
                }
            }
            return false;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    /**
     * Determines if the given field is optional. If no validation groups are provided,
     * it checks the field based on its validation annotations. If validation groups are provided,
     * it checks whether the field is mandatory in the context of those groups.
     *
     * @param field The field to check.
     * @return {@code true} if the field is optional, otherwise {@code false}.
     */
    public boolean isFieldOptional(Field field) {
        Annotation[] annotations = field.getAnnotations();

        // If no validation groups are provided, check annotations without considering groups.
        if (validationGroups.isEmpty()) {
            for (Annotation annotation : annotations) {
                if (isMandatoryAnnotation(annotation) && hasNoGroups(annotation)) {
                    return false;
                }
            }
            return true;
        }

        // If validation groups are provided, check annotations within the context of groups.
        for (Annotation annotation : annotations) {
            if (isMandatoryAnnotation(annotation) && (hasNoGroups(annotation) || matchesAnyGroup(annotation))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the field has any validation constraint annotations.
     *
     * A field is considered to have a validation constraint if any of its annotations
     * are marked with {@link Constraint}, and either:
     * - The annotation has no validation groups, or
     * - The annotation's groups match the provided validation groups.
     *
     * @param field the field to check
     * @return true if the field has a validation constraint, false otherwise
     */
    public boolean hasValidationConstraint(Field field) {
        if (field == null) {
            return false;
        }

        Annotation[] annotations = field.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Constraint.class) &&
                (hasNoGroups(annotation) || matchesAnyGroup(annotation))) {
                return true;
            }
        }

        return false;
    }
}
