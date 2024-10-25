package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Type mapping component for "Spring REST Docs Easy" that converts Java types
 * to Spring REST Docs JsonFieldType. Provides default mappings and customization
 * options for generating accurate API documentation.
 *
 * <p>Default mappings:</p>
 * - Collections/Maps: Map → OBJECT, Collection → ARRAY
 * - Primitives: boolean → BOOLEAN, numeric types → NUMBER
 * - Strings: String, CharSequence, char → STRING
 * - Date/Time: LocalDateTime, LocalDate, LocalTime → STRING
 * - Others: Enum, UUID, Currency, Locale → STRING
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * JsonFieldTypeMapper mapper = new JsonFieldTypeMapper();
 *
 * // Add custom type mapping
 * mapper.add(YourCustomType.class, JsonFieldType.STRING);
 *
 * // Override existing mapping
 * mapper.add(LocalDate.class, JsonFieldType.OBJECT);
 *
 * // Get type for documentation
 * JsonFieldType type = mapper.get(YourCustomType.class);
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
public class JsonFieldTypeMapper {
    private final Map<Class<?>, JsonFieldType> data = new HashMap<>();

    public JsonFieldTypeMapper() {
        // Define mappings for common types
        data.put(Map.class, JsonFieldType.OBJECT);
        data.put(Collection.class, JsonFieldType.ARRAY);
        data.put(Boolean.class, JsonFieldType.BOOLEAN);
        data.put(boolean.class, JsonFieldType.BOOLEAN);
        data.put(Number.class, JsonFieldType.NUMBER);

        // Primitive types
        data.put(byte.class, JsonFieldType.NUMBER);
        data.put(short.class, JsonFieldType.NUMBER);
        data.put(int.class, JsonFieldType.NUMBER);
        data.put(long.class, JsonFieldType.NUMBER);
        data.put(float.class, JsonFieldType.NUMBER);
        data.put(double.class, JsonFieldType.NUMBER);

        // String and related types
        data.put(String.class, JsonFieldType.STRING);
        data.put(CharSequence.class, JsonFieldType.STRING);
        data.put(Character.class, JsonFieldType.STRING);
        data.put(char.class, JsonFieldType.STRING);

        // Date and time types
        data.put(Date.class, JsonFieldType.STRING);
        data.put(Calendar.class, JsonFieldType.STRING);
        data.put(LocalDate.class, JsonFieldType.STRING);
        data.put(LocalDateTime.class, JsonFieldType.STRING);
        data.put(LocalTime.class, JsonFieldType.STRING);

        // Miscellaneous types
        data.put(Currency.class, JsonFieldType.STRING);
        data.put(Locale.class, JsonFieldType.STRING);
        data.put(Enum.class, JsonFieldType.STRING);
        data.put(UUID.class, JsonFieldType.STRING);
    }

    /**
     * Determines the JsonFieldType for a given Java class type.
     * Uses type hierarchy traversal to find the most appropriate mapping.
     *
     * <p>Resolution strategy:</p>
     * 1. If type is array → ARRAY
     * 2. If direct mapping exists → mapped type
     * 3. If assignable from mapped type → mapped type
     * 4. Default → OBJECT
     *
     * @param objectType the Class type to map to JsonFieldType
     * @return appropriate JsonFieldType for documentation
     */
    public JsonFieldType get(Class<?> objectType) {
        if (objectType.isArray()) {
            return JsonFieldType.ARRAY;
        }

        return data.entrySet().stream()
            .filter(entry -> entry.getKey().isAssignableFrom(objectType))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(JsonFieldType.OBJECT);
    }

    /**
     * Replaces all existing mappings with new ones.
     * Use with caution as this removes all default mappings.
     *
     * @param newData map of new type mappings to use
     */
    public void set(Map<Class<?>, JsonFieldType> newData) {
        data.clear();
        data.putAll(newData);
    }

    /**
     * Adds or updates a single type mapping.
     * If the type already exists, its mapping will be updated.
     *
     * @param type the Java type to map
     * @param jsonFieldType the corresponding documentation type
     */
    public void add(Class<?> type, JsonFieldType jsonFieldType) {
        data.put(type, jsonFieldType);
    }

    /**
     * Removes a type mapping if it exists.
     * Note: Removing core type mappings may affect documentation accuracy.
     *
     * @param type the type mapping to remove
     */
    public void remove(Class<?> type) {
        data.remove(type);
    }

    /**
     * Adds multiple type mappings at once.
     * Existing mappings for the same types will be overwritten.
     *
     * @param additionalData map of additional type mappings
     */
    public void addAll(Map<Class<?>, JsonFieldType> additionalData) {
        data.putAll(additionalData);
    }

    /**
     * Removes multiple type mappings at once.
     * Useful for bulk removal of custom mappings.
     *
     * @param typesToRemove collection of types whose mappings should be removed
     */
    public void removeAll(Collection<Class<?>> typesToRemove) {
        for (Class<?> type : typesToRemove) {
            data.remove(type);
        }
    }
}
