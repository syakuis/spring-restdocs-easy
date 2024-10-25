package io.github.syakuis.spring.restdocs.easy.core;

import lombok.Builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents comprehensive metadata about a field in a class for "Spring REST Docs Easy".
 * This record encapsulates field information needed for generating API documentation,
 * including field properties, class information, and annotations.
 *
 * <p>Features:</p>
 * - Complete field metadata for documentation generation
 * - Support for validation annotations processing
 * - Class and package information for context
 * - Reflection capabilities for field access
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ClassFieldMetadata metadata = ClassFieldMetadata.builder()
 *     .packageName("com.example.api")
 *     .className("UserDto")
 *     .name("email")
 *     .packageClassName("com.example.api.UserDto")
 *     .canonicalName("java.lang.String")
 *     .type(String.class)
 *     .target(UserDto.class)
 *     .field(UserDto.class.getDeclaredField("email"))
 *     .annotations(field.getAnnotations())
 *     .build();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-06-16
 *
 * @param packageName The package name of the class containing the field (e.g., "com.example.api")
 * @param className The simple name of the class containing the field (e.g., "UserDto")
 * @param name The field name as declared in the class (e.g., "email")
 * @param packageClassName The fully qualified class name (e.g., "com.example.api.UserDto")
 * @param canonicalName The canonical name of the field's type (e.g., "java.lang.String")
 * @param type The Class object representing the field's type (e.g., String.class)
 * @param target The Class object of the declaring class (e.g., UserDto.class)
 * @param field The Field object for reflection operations
 * @param annotations Array of annotations on the field (e.g., @NotNull, @Email)
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
