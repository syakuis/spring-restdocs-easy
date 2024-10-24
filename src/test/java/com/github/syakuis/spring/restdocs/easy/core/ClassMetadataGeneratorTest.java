package com.github.syakuis.spring.restdocs.easy.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
@Slf4j
class ClassMetadataGeneratorTest {
    @Test
    void shouldLoadFieldsFromAbstractClass() {
        List<ClassFieldMetadata> metadata = ClassMetadataGenerator.of(AbstractAddress.class).toList();
        List<String> fieldNames = metadata.stream()
            .map(ClassFieldMetadata::name).toList();

        assertEquals(List.of("zipcode"), fieldNames);
    }

    @Test
    void shouldLoadEnumClass() {
        List<ClassFieldMetadata> metadata = ClassMetadataGenerator.of(Sex.class).toList();
        var dataClassMetadata = metadata.stream().filter(it -> Objects.equals("sex", it.name()))
            .findFirst().orElseThrow();

        assertEquals("sex", dataClassMetadata.name());
    }

    @Test
    void shouldLoadFieldsFromUserClass() {
        List<ClassFieldMetadata> metadata = ClassMetadataGenerator.of(User.class).toList();

        List<String> fieldNames = metadata.stream()
            .map(ClassFieldMetadata::name)
            .toList();

        assertFalse(fieldNames.contains("CONSTANT"));
        assertFalse(fieldNames.contains("isMale"));
        assertTrue(fieldNames.contains("name"));
        assertTrue(fieldNames.contains("username"));

        ClassFieldMetadata usernameMetadata = metadata.stream()
            .filter(m -> "username".equals(m.name()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Username field not found"));

        assertNotNull(usernameMetadata);
        assertNotEquals(0, usernameMetadata.annotations().length);
        assertEquals("com.github.syakuis.spring.restdocs.easy.core.ClassMetadataGeneratorTest$User.username",
            usernameMetadata.packageClassName() + "." + usernameMetadata.name());

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