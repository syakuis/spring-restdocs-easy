package com.github.syakuis.spring.restdocs.easy.core;

import lombok.Builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-06-16
 */
@Builder
public record DataClassMetadata(
    String packageName,
    String className,
    // todo rename name
    String fieldName,
    // todo rename packageClassName (message properties에 사용)
    String name,
    String canonicalName,
    // todo rename type
    Class<?> fieldType,
    Class<?> target,
    Field field,
    Annotation[] annotations
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataClassMetadata that = (DataClassMetadata) o;
        return Objects.equals(name, that.name) && Objects.equals(field, that.field) && Objects.equals(target, that.target) && Objects.equals(className, that.className) && Objects.equals(fieldName, that.fieldName) && Objects.equals(packageName, that.packageName) && Objects.equals(fieldType, that.fieldType) && Objects.equals(canonicalName, that.canonicalName) && Arrays.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(packageName);
        result = 31 * result + Objects.hashCode(className);
        result = 31 * result + Objects.hashCode(fieldName);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(canonicalName);
        result = 31 * result + Objects.hashCode(fieldType);
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
            ", fieldName='" + fieldName + '\'' +
            ", name='" + name + '\'' +
            ", canonicalName='" + canonicalName + '\'' +
            ", fieldType=" + fieldType +
            ", target=" + target +
            ", field=" + field +
            ", annotations=" + Arrays.toString(annotations) +
            '}';
    }
}
