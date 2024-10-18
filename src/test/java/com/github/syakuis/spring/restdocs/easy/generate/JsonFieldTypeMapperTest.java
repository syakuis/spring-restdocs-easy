package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
class JsonFieldTypeMapperTest {

    @Test
    void testMapTypes() {
        assertEquals(JsonFieldType.OBJECT, JsonFieldTypeMapper.get(Map.class));
        assertEquals(JsonFieldType.ARRAY, JsonFieldTypeMapper.get(Collection.class));
    }

    @Test
    void testBooleanTypes() {
        assertEquals(JsonFieldType.BOOLEAN, JsonFieldTypeMapper.get(Boolean.class));
        assertEquals(JsonFieldType.BOOLEAN, JsonFieldTypeMapper.get(boolean.class));
    }

    @Test
    void testNumberTypes() {
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(Number.class));
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(byte.class));
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(short.class));
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(int.class));
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(long.class));
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(float.class));
        assertEquals(JsonFieldType.NUMBER, JsonFieldTypeMapper.get(double.class));
    }

    @Test
    void testStringTypes() {
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(String.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(CharSequence.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(Character.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(char.class));
    }

    @Test
    void testDateAndTimeTypes() {
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(Date.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(Calendar.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(LocalDate.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(LocalDateTime.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(LocalTime.class));
    }

    @Test
    void testMiscellaneousTypes() {
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(Currency.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(Locale.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(EnumExample.class));
        assertEquals(JsonFieldType.STRING, JsonFieldTypeMapper.get(UUID.class));
    }

    enum EnumExample {
        EXAMPLE1,
        EXAMPLE2
    }

    @Test
    void get() {
        Map<String, JsonFieldType> expectedMappings = new HashMap<>();
        expectedMappings.put("dataMap", JsonFieldType.OBJECT);
        expectedMappings.put("stringList", JsonFieldType.ARRAY);
        expectedMappings.put("stringArray", JsonFieldType.ARRAY);
        expectedMappings.put("isActive", JsonFieldType.BOOLEAN);
        expectedMappings.put("isVerified", JsonFieldType.BOOLEAN);
        expectedMappings.put("genericNumber", JsonFieldType.NUMBER);
        expectedMappings.put("integerNumber", JsonFieldType.NUMBER);
        expectedMappings.put("doubleNumber", JsonFieldType.NUMBER);
        expectedMappings.put("name", JsonFieldType.STRING);
        expectedMappings.put("description", JsonFieldType.STRING);
        expectedMappings.put("initial", JsonFieldType.STRING);
        expectedMappings.put("birthDate", JsonFieldType.STRING);
        expectedMappings.put("localDate", JsonFieldType.STRING);
        expectedMappings.put("localDateTime", JsonFieldType.STRING);
        expectedMappings.put("currency", JsonFieldType.STRING);
        expectedMappings.put("locale", JsonFieldType.STRING);
        expectedMappings.put("uniqueId", JsonFieldType.STRING);
        expectedMappings.put("status", JsonFieldType.STRING);

        // Get declared fields from the class
        Field[] fields = JsonFieldTypeData.class.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            JsonFieldType actualType = JsonFieldTypeMapper.get(fieldType);

            // Assert that the actual type matches the expected type
            assertEquals(expectedMappings.get(fieldName), actualType,
                "Mismatch for field: " + fieldName);
        }
    }

    static class JsonFieldTypeData {
        // Object type
        public Map<String, Object> dataMap;

        // Array type
        public List<String> stringList;
        public String[] stringArray;

        // Boolean types
        public Boolean isActive;
        public boolean isVerified;

        // Number types
        public Number genericNumber;
        public int integerNumber;
        public double doubleNumber;

        // String and related types
        public String name;
        public CharSequence description;
        public char initial;

        // Date and time types
        public Date birthDate;
        public LocalDate localDate;
        public LocalDateTime localDateTime;

        // Miscellaneous types
        public Currency currency;
        public Locale locale;
        public UUID uniqueId;

        // Enum type
        public TestEnum status;

        // Example enum for testing
        enum TestEnum {
            ACTIVE, INACTIVE
        }
    }
}