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

    private boolean isRecordGetter(Class<?> target, String fieldName, Class<?> fieldType) {
        if (fieldName.isBlank() || target == null) {
            return false;
        }

        return isMethod(target, fieldName, fieldType);
    }

    private boolean isGetter(Class<?> target, String fieldName, Class<?> fieldType) {
        if (fieldName.isBlank() || target == null) {
            return false;
        }

        // getter method
        var methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // fluent method
        if (isMethod(target, fieldName, fieldType)) {
            return true;
        } else if (isMethod(target, "get" + methodName, fieldType)) {
            return true;
        } else return isMethod(target, "is" + methodName, fieldType);
    }

    private boolean isMethod(Class<?> target, String methodName, Class<?> fieldType) {
        try {
            var method = target.getMethod(methodName);
            return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(fieldType);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

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