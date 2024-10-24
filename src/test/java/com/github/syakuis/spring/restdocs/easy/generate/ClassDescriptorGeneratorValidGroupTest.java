package com.github.syakuis.spring.restdocs.easy.generate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class ClassDescriptorGeneratorValidGroupTest {

    @Mock
    private MessageSource messageSource;


    @Test
    void testGenerateWithValidGroups() {
        // Given
        Class<?> targetClass = SampleClass.class;

        ClassDescriptorGenerator classDescriptorGenerator = new ClassDescriptorGenerator(messageSource, new JsonFieldTypeMapper(), targetClass);

        // When
        List<Descriptor> descriptors = classDescriptorGenerator.generate(SampleClass.BasicValidation.class);

        descriptors.forEach(it -> {
            if (Objects.equals("name", it.name())) {
                assertFalse(it.optional());
                assertEquals(1, it.attributes().length);
                Arrays.stream(it.attributes())
                    .forEach(attribute -> assertEquals("Must not be null", attribute.getValue()));
            }

            if (Objects.equals("age", it.name())) {
                assertTrue(it.optional());
                assertEquals(0, it.attributes().length);
            }
        });
    }

    record SampleClass(
        @NotNull(groups = BasicValidation.class)
        String name,

        @Min(value = 18, groups = AgeValidation.class)
        int age,

        @Email(groups = BasicValidation.class)
        String email
    ) {
        public interface BasicValidation {}
        public interface AgeValidation {}
    }
}