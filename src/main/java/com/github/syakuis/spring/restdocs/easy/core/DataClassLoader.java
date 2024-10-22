package com.github.syakuis.spring.restdocs.easy.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-06-16
 */
// todo - add enum loader and various class types must be implemented as an abstract class
public final class DataClassLoader {
    private final Class<?> targetClass;

    private DataClassLoader(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    private boolean isRecordGetter(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        if (fieldName.isBlank() || targetClass == null) {
            return false;
        }

        return isMethod(targetClass, fieldName, fieldType);
    }

    private boolean isGetter(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        if (fieldName.isBlank() || targetClass == null) {
            return false;
        }

        // getter method
        var methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // fluent method
        if (isMethod(targetClass, fieldName, fieldType)) {
            return true;
        } else if (isMethod(targetClass, "get" + methodName, fieldType)) {
            return true;
        } else return isMethod(targetClass, "is" + methodName, fieldType);
    }

    private boolean isMethod(Class<?> targetClass, String methodName, Class<?> fieldType) {
        try {
            var method = targetClass.getMethod(methodName);
            return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(fieldType);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Generates a list of field metadata for the target class.
     *
     * This method processes only fields that meet the following conditions:
     * 1. Fields of enum type
     * 2. Fields with a getter method
     *    - For regular classes, {@link #isGetter(Class, String, Class)} is used to check for the presence of a getter method.
     *    - For record classes, {@link #isRecordGetter(Class, String, Class)} is used to check for the presence of a getter method.
     *
     * Important notes:
     * - Fields without a getter method are not processed.
     * - Inherited fields are not processed. Only fields explicitly declared in the target class are processed.
     * - Fields with private, protected, or default access modifiers are only accessible through their getter methods.
     *
     * @return A list of field metadata
     */
    public List<DataClassMetadata> toList() {
        var packageName = targetClass.getPackageName();
        var className = targetClass.getSimpleName();
        var name = targetClass.getName();
        var canonicalName = targetClass.getCanonicalName();

        Field[] fields = targetClass.getDeclaredFields();

        TriPredicate<Class<?>, String, Class<?>> isGetter = targetClass.isRecord() ?
            this::isRecordGetter :
            this::isGetter;

        return Arrays.stream(fields)
            .filter(field -> field.getType().isEnum() || isGetter.test(targetClass, field.getName(), field.getType()))
            .map(field -> new DataClassMetadata(
                packageName,
                className,
                field.getName(),
                name,
                canonicalName,
                field.getType(),
                targetClass,
                field,
                field.getAnnotations()))
            .toList();
    }

    public static DataClassLoader of(Class<?> targetClass) {
        return new DataClassLoader(targetClass);
    }
}