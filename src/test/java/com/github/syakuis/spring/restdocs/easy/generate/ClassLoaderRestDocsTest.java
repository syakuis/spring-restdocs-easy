package com.github.syakuis.spring.restdocs.easy.generate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-21
 */
@WebMvcTest
@Import({MessageSourceAutoConfiguration.class})
class ClassLoaderRestDocsTest {
    @Autowired
    private MessageSource messageSource;

    @Test
    void getMessage() {
        var result = new ClassLoaderRestDocs(messageSource, Sample.class).generate();

        result.stream().filter(it -> it.name().equals("name")).forEach(it -> {
            assertEquals("테스트", it.description());
            assertTrue(it.optional());
        });
    }

    @Test
    void testOptionalField() {
        var result = new ClassLoaderRestDocs(messageSource, SampleWithOptionalField.class).generate();

        result.stream().filter(it -> it.name().equals("optionalField")).forEach(it -> {
            assertTrue(it.optional());
        });
    }

    @Test
    void testFieldWithoutConstraints() {
        var result = new ClassLoaderRestDocs(messageSource, SampleWithoutConstraints.class).generate();

        result.stream().filter(it -> it.name().equals("noConstraintField")).forEach(it -> {
            assertEquals(0, it.attributes().length);
        });
    }

    @Test
    void testEnumField() {
        var result = new ClassLoaderRestDocs(messageSource, SampleWithEnum.class).generate();

        result.stream().filter(it -> it.name().equals("enumField")).forEach(it -> {
            assertTrue(it.description().toString().contains("ENUM_CONSTANT"));
        });
    }

    @Test
    void testFieldWithNotNullConstraint() {
        var result = new ClassLoaderRestDocs(messageSource, SampleWithNotNull.class).generate();

        result.stream().filter(it -> it.name().equals("notNullField")).forEach(it -> {
            assertTrue(it.attributes().length > 0);
            assertTrue(Arrays.stream(it.attributes()).anyMatch(attr ->
                attr.getValue().equals("Must not be null")));
        });
    }

    @Test
    void testFieldWithSizeConstraint() {
        var result = new ClassLoaderRestDocs(messageSource, SampleWithSize.class).generate();

        result.stream().filter(it -> it.name().equals("sizeField")).forEach(it -> {
            assertTrue(it.attributes().length > 0);  // Check that constraints are present
            assertTrue(Arrays.stream(it.attributes()).anyMatch(attr ->
                attr.getValue().toString().contains("Size must be between 2 and 10 inclusive")));  // Check for Size message
        });
    }

    // Sample record class for testing
    record Sample(String name) {
    }

    // Sample class with an optional field
    record SampleWithOptionalField(String optionalField) {
    }

    // Sample class without constraints
    record SampleWithoutConstraints(String noConstraintField) {
    }

    // Sample class with enum field
    record SampleWithEnum(TestEnum enumField) {
    }

    // Sample enum for testing enum fields
    enum TestEnum {
        ENUM_CONSTANT
    }

    // Sample class with a NotNull constraint
    record SampleWithNotNull(
        @NotNull String notNullField
    ) {
    }

    // Sample class with a Size constraint
    record SampleWithSize(
        @Size(min = 2, max = 10) String sizeField
    ) {
    }
}