package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
public class JsonFieldTypeMapper {
    private static final Map<Class<?>, JsonFieldType> data = new HashMap<>();

    private JsonFieldTypeMapper() {
    }

    static {
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

    public static JsonFieldType get(Class<?> objectType) {
        if (objectType.isArray()) {
            return JsonFieldType.ARRAY;
        }

        for (Map.Entry<Class<?>, JsonFieldType> entry : data.entrySet()) {
            if (entry.getKey().isAssignableFrom(objectType)) {
                return entry.getValue();
            }
        }

        return JsonFieldType.OBJECT;
    }
}
