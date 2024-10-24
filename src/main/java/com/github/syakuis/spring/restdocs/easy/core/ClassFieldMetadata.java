package com.github.syakuis.spring.restdocs.easy.core;

import lombok.Builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents metadata information about a field in a class.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-06-16
 * @param packageName The package name of the class containing the field
 * @param className The simple name of the class containing the field
 * @param name The name of the field
 * @param packageClassName The fully qualified name of the class containing the field,
 *                         in the format "package.ClassName"
 * @param canonicalName The canonical name of the field's type
 * @param type The {@link Class} object representing the field's type
 * @param target The {@link Class} object representing the class that declares the field
 * @param field The {@link Field} object representing the field
 * @param annotations An array of {@link Annotation} objects representing the annotations
 *                    present on the field
 */
@Builder
public record ClassFieldMetadata(
    String packageName,
    String className,
    String name,
    String packageClassName,
    String canonicalName,
    Class<?> type,
    Class<?> target,
    Field field,
    Annotation[] annotations
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassFieldMetadata that = (ClassFieldMetadata) o;
        return Objects.equals(packageClassName, that.packageClassName)
            && Objects.equals(field, that.field)
            && Objects.equals(target, that.target)
            && Objects.equals(className, that.className)
            && Objects.equals(name, that.name)
            && Objects.equals(packageName, that.packageName)
            && Objects.equals(type, that.type)
            && Objects.equals(canonicalName, that.canonicalName)
            && Arrays.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(packageName);
        result = 31 * result + Objects.hashCode(className);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(packageClassName);
        result = 31 * result + Objects.hashCode(canonicalName);
        result = 31 * result + Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(target);
        result = 31 * result + Objects.hashCode(field);
        result = 31 * result + Arrays.hashCode(annotations);
        return result;
    }

    @Override
    public String toString() {
        return "DataClassMetadata{" +
            "packageName='" + packageName + '\'' +
            ", className='" + className + '\'' +
            ", name='" + name + '\'' +
            ", packageClassName='" + packageClassName + '\'' +
            ", canonicalName='" + canonicalName + '\'' +
            ", type=" + type +
            ", target=" + target +
            ", field=" + field +
            ", annotations=" + Arrays.toString(annotations) +
            '}';
    }
}
