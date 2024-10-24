package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * JsonFieldTypeMapper is a utility class that serves as a mapper for Java Class types to
 * their corresponding JsonFieldType values as defined by Spring REST Docs.
 * <p>
 * This class allows for managing the mapping of various Java types (e.g., primitives,
 * collections, date types) not only for generating JSON representations but also for
 * mapping parameter types in RESTful applications, facilitating type recognition and
 * consistent handling of data formats.
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
     * Returns the corresponding JsonFieldType for a given Class type.
     *
     * @param objectType the Class type for which the JsonFieldType is requested
     * @return JsonFieldType corresponding to the provided Class type
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
     * Sets the mapping data to a new set of mappings.
     *
     * @param newData a Map containing new Class to JsonFieldType mappings
     */
    public void set(Map<Class<?>, JsonFieldType> newData) {
        data.clear();
        data.putAll(newData);
    }

    /**
     * Adds a new mapping for a specific Class type to JsonFieldType.
     *
     * @param type the Class type to be added
     * @param jsonFieldType the corresponding JsonFieldType for the Class type
     */
    public void add(Class<?> type, JsonFieldType jsonFieldType) {
        data.put(type, jsonFieldType);
    }

    /**
     * Removes a specific mapping for a given Class type.
     *
     * @param type the Class type to be removed from the mapping
     */
    public void remove(Class<?> type) {
        data.remove(type);
    }

    /**
     * Adds all mappings from an additional set of Class to JsonFieldType mappings.
     *
     * @param additionalData a Map containing additional Class to JsonFieldType mappings
     */
    public void addAll(Map<Class<?>, JsonFieldType> additionalData) {
        data.putAll(additionalData);
    }

    /**
     * Removes multiple mappings from the existing set based on the provided collection of Class types.
     *
     * @param typesToRemove a Collection of Class types to be removed from the mapping
     */
    public void removeAll(Collection<Class<?>> typesToRemove) {
        for (Class<?> type : typesToRemove) {
            data.remove(type);
        }
    }
}
