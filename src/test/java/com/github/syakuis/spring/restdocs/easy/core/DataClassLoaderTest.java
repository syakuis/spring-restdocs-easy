package com.github.syakuis.spring.restdocs.easy.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
@Slf4j
class DataClassLoaderTest {
    @Test
    void shouldLoadFieldsFromAbstractClass() {
        List<DataClassMetadata> metadata = DataClassLoader.of(AbstractAddress.class).toList();
        List<String> fieldNames = metadata.stream()
            .map(DataClassMetadata::fieldName).toList();

        assertEquals(List.of("zipcode"), fieldNames);
    }

    @Test
    void shouldLoadEnumClass() {
        List<DataClassMetadata> metadata = DataClassLoader.of(Sex.class).toList();
        List<String> fieldNames = metadata.stream()
            .map(DataClassMetadata::fieldName).toList();

        assertEquals(List.of("MALE", "FEMALE"), fieldNames);
    }

    @Test
    void shouldLoadFieldsFromUserClass() {
        List<DataClassMetadata> metadata = DataClassLoader.of(User.class).toList();
        List<String> fieldNames = metadata.stream()
            .map(DataClassMetadata::fieldName)
            .toList();

        assertFalse(fieldNames.contains("CONSTANT"));
        assertFalse(fieldNames.contains("isMale"));
        assertTrue(fieldNames.contains("name"));
        assertTrue(fieldNames.contains("username"));

        DataClassMetadata usernameMetadata = metadata.stream()
            .filter(m -> "username".equals(m.fieldName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Username field not found"));

        assertNotNull(usernameMetadata);
        assertNotEquals(0, usernameMetadata.annotations().length);

        boolean hasNotNullAnnotation = Arrays.stream(usernameMetadata.annotations())
            .anyMatch(annotation -> annotation.annotationType().equals(NotNull.class));

        assertTrue(hasNotNullAnnotation, "Username field should have @NotNull annotation");
    }

    enum Sex {
        MALE, FEMALE
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    static class User {
        private String name;

        @NotNull
        private String username;

        private boolean member;
        private Sex sex;
        private int age;
        private boolean adult;

        public static final String CONSTANT = "상수";

        public String name() {
            return name;
        }

        public boolean isMale() {
            return sex == Sex.MALE;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Accessors(fluent = true)
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    abstract static class AbstractAddress {
        private String zipcode;
    }
}