package io.github.syakuis.spring.restdocs.easy.core;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Core metadata generator for "Spring REST Docs Easy" that analyzes class fields
 * using reflection to create documentation-ready metadata.
 *
 * <p>Key features:</p>
 * - Generates metadata for fields in regular classes and record classes
 * - Special handling for Enum types
 * - Supports different getter method patterns (standard, fluent, and boolean)
 * - Processes only fields with proper getter methods or Enum types
 * - Integration with validation annotations for documentation
 *
 * <p>The metadata generation process follows these steps:</p>
 * 1. Retrieve all declared fields of the target class
 * 2. For each field, verify:
 *    - If it's an Enum type
 *    - If it has a valid getter method:
 *      - For regular classes: checks standard, fluent, and boolean getter patterns
 *      - For record classes: checks canonical accessor methods
 * 3. Generate {@link ClassFieldMetadata} for qualifying fields
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Generate metadata for a DTO class
 * List<ClassFieldMetadata> metadata = ClassMetadataGenerator
 *     .of(UserDto.class)
 *     .toList();
 *
 * // Generate metadata for an Enum
 * List<ClassFieldMetadata> enumMetadata = ClassMetadataGenerator
 *     .of(UserStatus.class)
 *     .toList();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-06-16
 */
public final class ClassMetadataGenerator {
    private final Class<?> targetClass;

    /**
     * The constructor is declared as private, so instances cannot be directly created from outside the class.
     * Instead, instances should be created using the {@link #of(Class)} method.
     *
     * @param targetClass The target class for which metadata will be generated
     */
    private ClassMetadataGenerator(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Checks if a method is a getter method in a record class.
     * Record classes have automatically generated canonical accessor methods that match the field names exactly.
     * <p>
     * For example, for a record field 'name':
     * - The getter method will be exactly 'name()'
     *
     * @param targetClass The class to check for the getter method
     * @param fieldName The name of the field to find a getter for
     * @param fieldType The expected return type of the getter method
     * @return true if a matching getter method exists, false otherwise
     */
    private boolean isRecordGetter(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        if (fieldName.isBlank() || targetClass == null) {
            return false;
        }

        return isMethod(targetClass, fieldName, fieldType);
    }

    /**
     * Checks if a method is a getter method in a regular class.
     * Supports three getter method patterns:
     * <p>
     * 1. Fluent style: field 'name' → method 'name()'
     * 2. Standard style: field 'name' → method 'getName()'
     * 3. Boolean style: field 'active' → method 'isActive()'
     *
     * @param targetClass The class to check for the getter method
     * @param fieldName The name of the field to find a getter for
     * @param fieldType The expected return type of the getter method
     * @return true if any matching getter pattern is found, false otherwise
     */
    private boolean isGetter(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        if (fieldName.isBlank() || targetClass == null) {
            return false;
        }

        // Generate getter method name
        var methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // Check for fluent method
        if (isMethod(targetClass, fieldName, fieldType)) {
            return true;
        } else if (isMethod(targetClass, "get" + methodName, fieldType)) {
            return true;
        } else return isMethod(targetClass, "is" + methodName, fieldType);
    }

    /**
     * Verifies the existence of a public method with specified name and return type.
     * This method checks:
     * - Method existence
     * - Public access modifier
     * - Matching return type
     *
     * @param targetClass The class to check for the method
     * @param methodName The exact name of the method to find
     * @param fieldType The expected return type of the method
     * @return true if a matching public method exists, false otherwise
     */
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
     * <p>
     * Processing behavior:
     * - For Enum classes: Creates a single metadata entry representing the Enum itself
     * - For regular/record classes: Creates metadata for each qualifying field
     * <p>
     * Field qualification rules:
     * 1. Must be either:
     *    - An Enum type field
     *    - A field with a valid getter method
     * 2. Only processes directly declared fields (inherited fields are excluded)
     * 3. Access modifiers are considered through getter method availability
     *
     * @return A list of {@link ClassFieldMetadata} objects representing the qualifying fields
     */
    public List<ClassFieldMetadata> toList() {
        var packageName = targetClass.getPackageName();
        var className = targetClass.getSimpleName();
        var name = targetClass.getName();
        var canonicalName = targetClass.getCanonicalName();

        Field[] fields = targetClass.getDeclaredFields();

        TriPredicate<Class<?>, String, Class<?>> isGetter = targetClass.isRecord() ?
            this::isRecordGetter :
            this::isGetter;

        if (targetClass.isEnum()) {
            return List.of(
                new ClassFieldMetadata(
                    packageName,
                    className,
                    StringUtils.uncapitalize(className),
                    name,
                    canonicalName,
                    targetClass,
                    targetClass,
                    null,
                    null)
            );
        }

        return Arrays.stream(fields)
            .filter(field -> field.getType().isEnum() || isGetter.test(targetClass, field.getName(), field.getType()))
            .map(field -> new ClassFieldMetadata(
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

    /**
     * Factory method to create a new instance of ClassMetadataGenerator.
     * This is the preferred way to instantiate this class instead of using the private constructor directly.
     *
     * @param targetClass The class to analyze for metadata generation
     * @return A new instance of ClassMetadataGenerator configured for the target class
     */
    public static ClassMetadataGenerator of(Class<?> targetClass) {
        return new ClassMetadataGenerator(targetClass);
    }
}