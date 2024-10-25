package io.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Nested;
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
    private final JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();

    @Test
    void testMapTypes() {
        assertEquals(JsonFieldType.OBJECT, jsonFieldTypeMapper.get(Map.class));
        assertEquals(JsonFieldType.ARRAY, jsonFieldTypeMapper.get(Collection.class));
    }

    @Test
    void testBooleanTypes() {
        assertEquals(JsonFieldType.BOOLEAN, jsonFieldTypeMapper.get(Boolean.class));
        assertEquals(JsonFieldType.BOOLEAN, jsonFieldTypeMapper.get(boolean.class));
    }

    @Test
    void testNumberTypes() {
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(Number.class));
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(byte.class));
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(short.class));
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(int.class));
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(long.class));
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(float.class));
        assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(double.class));
    }

    @Test
    void testStringTypes() {
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(String.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(CharSequence.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(Character.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(char.class));
    }

    @Test
    void testDateAndTimeTypes() {
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(Date.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(Calendar.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(LocalDate.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(LocalDateTime.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(LocalTime.class));
    }

    @Test
    void testMiscellaneousTypes() {
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(Currency.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(Locale.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(EnumExample.class));
        assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(UUID.class));
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
            JsonFieldType actualType = jsonFieldTypeMapper.get(fieldType);

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

    @Nested
    class MapTest {

        @Test
        void testAdd() {
            JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();
            // Define a new mapping
            jsonFieldTypeMapper.add(String.class, JsonFieldType.STRING);

            // Verify that the mapping has been added
            assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(String.class));
        }

        @Test
        void testAddAll() {
            JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();
            // Define multiple mappings to add
            Map<Class<?>, JsonFieldType> additionalData = new HashMap<>();
            additionalData.put(Integer.class, JsonFieldType.NUMBER);
            additionalData.put(Double.class, JsonFieldType.NUMBER);

            // Add them all at once
            jsonFieldTypeMapper.addAll(additionalData);

            // Verify that the mappings have been correctly added
            assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(Integer.class));
            assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(Double.class));
        }

        @Test
        void testRemove() {
            JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();
            // Add a mapping to remove
            jsonFieldTypeMapper.add(Boolean.class, JsonFieldType.BOOLEAN);

            // Verify the mapping is present
            assertEquals(JsonFieldType.BOOLEAN, jsonFieldTypeMapper.get(Boolean.class));

            // Remove the mapping
            jsonFieldTypeMapper.remove(Boolean.class);

            // Verify the mapping is no longer present
            assertEquals(JsonFieldType.OBJECT, jsonFieldTypeMapper.get(Boolean.class));
        }

        @Test
        void testRemoveAll() {
            JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();
            // Add multiple mappings to remove
            jsonFieldTypeMapper.add(String.class, JsonFieldType.STRING);
            jsonFieldTypeMapper.add(Integer.class, JsonFieldType.NUMBER);

            // Verify that the mappings exist
            assertEquals(JsonFieldType.STRING, jsonFieldTypeMapper.get(String.class));
            assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(Integer.class));

            // Remove all mappings
            jsonFieldTypeMapper.removeAll(Arrays.asList(String.class, CharSequence.class, Number.class, Integer.class));

            // Verify that the mappings are no longer present
            assertEquals(JsonFieldType.OBJECT, jsonFieldTypeMapper.get(String.class));
            assertEquals(JsonFieldType.OBJECT, jsonFieldTypeMapper.get(Integer.class));
        }

        @Test
        void testSet() {
            JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();
            // Create a new mapping to set
            Map<Class<?>, JsonFieldType> newData = new HashMap<>();
            newData.put(Long.class, JsonFieldType.NUMBER);
            newData.put(Float.class, JsonFieldType.NUMBER);

            // Set the new data
            jsonFieldTypeMapper.set(newData);

            // Verify that the new mappings are present
            assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(Long.class));
            assertEquals(JsonFieldType.NUMBER, jsonFieldTypeMapper.get(Float.class));

            // Verify that old mappings are cleared
            assertEquals(JsonFieldType.OBJECT, jsonFieldTypeMapper.get(String.class));
        }
    }
}