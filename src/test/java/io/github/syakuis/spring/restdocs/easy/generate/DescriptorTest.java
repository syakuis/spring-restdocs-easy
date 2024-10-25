package io.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
class DescriptorTest {
    @Test
    void testDefaultValues() {
        Descriptor descriptor = Descriptor.builder()
            .name("Test Name")
            .description("This is a test description")
            .build();

        // 기본값 검증
        assertEquals("Test Name", descriptor.name());
        assertEquals(JsonFieldType.STRING, descriptor.type()); // 기본값 확인
        assertEquals("This is a test description", descriptor.description());
        assertFalse(descriptor.optional()); // 기본값 확인
        assertFalse(descriptor.ignore()); // 기본값 확인
    }

    @Test
    void testCustomValues() {
        // 커스텀 값으로 Description 객체 생성
        Descriptor customDescriptor = Descriptor.builder()
            .name("Custom Name")
            .description("Custom description")
            .type(JsonFieldType.NUMBER) // 기본값을 덮어씀
            .optional(true) // 기본값을 덮어씀
            .ignore(true) // 기본값을 덮어씀
            .build();

        // 커스텀 값 검증
        assertEquals("Custom Name", customDescriptor.name());
        assertEquals(JsonFieldType.NUMBER, customDescriptor.type()); // 커스텀 값 확인
        assertEquals("Custom description", customDescriptor.description());
        assertTrue(customDescriptor.optional()); // 커스텀 값 확인
        assertTrue(customDescriptor.ignore()); // 커스텀 값 확인
    }

}