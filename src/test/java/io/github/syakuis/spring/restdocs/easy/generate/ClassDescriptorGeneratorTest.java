package io.github.syakuis.spring.restdocs.easy.generate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-21
 */
@WebMvcTest
@Import({MessageSourceAutoConfiguration.class})
class ClassDescriptorGeneratorTest {
    @Autowired
    private MessageSource messageSource;

    private final JsonFieldTypeMapper jsonFieldTypeMapper = new JsonFieldTypeMapper();

    @Test
    void testColor() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(Color.class);

        result.forEach(it -> {
            if (it.name().equals("background")) {
                assertEquals("배경색", it.description());
            } else if (it.name().equals("font")) {
                assertEquals("글자색", it.description());
            }
        });
    }

    @Test
    void getMessage() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(Sample.class);

        result.stream().filter(it -> it.name().equals("name")).forEach(it -> {
            assertEquals("테스트", it.description());
            assertTrue(it.optional());
        });
    }

    @Test
    void testOptionalField() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(SampleWithOptionalField.class);

        result.stream().filter(it -> it.name().equals("optionalField")).forEach(it -> {
            assertTrue(it.optional());
        });
    }

    @Test
    void testFieldWithoutConstraints() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(SampleWithoutConstraints.class);

        result.stream().filter(it -> it.name().equals("noConstraintField")).forEach(it -> {
            assertEquals(0, it.attributes().length);
        });
    }

    @Test
    void testEnumField() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(SampleWithEnum.class);

        result.stream().filter(it -> it.name().equals("enumField")).forEach(it -> {
            assertTrue(it.description().toString().contains("ENUM_CONSTANT"));
        });
    }

    @Test
    void testFieldWithNotNullConstraint() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(SampleWithNotNull.class);

        result.stream().filter(it -> it.name().equals("notNullField")).forEach(it -> {
            assertTrue(it.attributes().length > 0);
            assertTrue(Arrays.stream(it.attributes()).anyMatch(attr ->
                attr.getValue().equals("Must not be null")));
        });
    }

    @Test
    void testFieldWithSizeConstraint() {
        var result = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper).generate(SampleWithSize.class);

        result.stream().filter(it -> it.name().equals("sizeField")).forEach(it -> {
            assertTrue(it.attributes().length > 0);  // Check that constraints are present
            assertTrue(Arrays.stream(it.attributes()).anyMatch(attr ->
                attr.getValue().toString().contains("Size must be between 2 and 10 inclusive")));  // Check for Size message
        });
    }

    @Test
    void testJsonFieldTypeMapping() {
        JsonFieldTypeMapper customMapper = new JsonFieldTypeMapper();
        customMapper.add(String.class, JsonFieldType.STRING);
        customMapper.add(Integer.class, JsonFieldType.NUMBER);
        customMapper.add(Boolean.class, JsonFieldType.BOOLEAN);
        customMapper.add(List.class, JsonFieldType.ARRAY);

        record TestClass(
            String stringField,
            Integer intField,
            Boolean boolField,
            List<String> listField,
            LocalDate dateField
        ) {}

        var result = new ClassDescriptorGenerator(messageSource, customMapper).generate(TestClass.class);

        result.forEach(descriptor -> {
            switch (descriptor.name()) {
                case "stringField":
                    assertEquals(JsonFieldType.STRING, descriptor.type());
                    break;
                case "intField":
                    assertEquals(JsonFieldType.NUMBER, descriptor.type());
                    break;
                case "boolField":
                    assertEquals(JsonFieldType.BOOLEAN, descriptor.type());
                    break;
                case "listField":
                    assertEquals(JsonFieldType.ARRAY, descriptor.type());
                    break;
                case "dateField":
                    // LocalDate는 기본적으로 STRING으로 매핑됩니다 (JsonFieldTypeMapper의 기본 설정에 따라)
                    assertEquals(JsonFieldType.STRING, descriptor.type());
                    break;
                default:
                    fail("Unexpected field: " + descriptor.name());
            }
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