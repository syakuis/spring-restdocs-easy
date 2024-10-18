package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
class DescriptionTest {
    @Test
    void testDefaultValues() {
        Description description = Description.builder()
            .name("Test Name")
            .desc("This is a test description")
            .build();

        // 기본값 검증
        assertEquals("Test Name", description.name());
        assertEquals(JsonFieldType.STRING, description.type()); // 기본값 확인
        assertEquals("This is a test description", description.desc());
        assertFalse(description.optional()); // 기본값 확인
        assertFalse(description.ignore()); // 기본값 확인
    }

    @Test
    void testCustomValues() {
        // 커스텀 값으로 Description 객체 생성
        Description customDescription = Description.builder()
            .name("Custom Name")
            .desc("Custom description")
            .type(JsonFieldType.NUMBER) // 기본값을 덮어씀
            .optional(true) // 기본값을 덮어씀
            .ignore(true) // 기본값을 덮어씀
            .build();

        // 커스텀 값 검증
        assertEquals("Custom Name", customDescription.name());
        assertEquals(JsonFieldType.NUMBER, customDescription.type()); // 커스텀 값 확인
        assertEquals("Custom description", customDescription.desc());
        assertTrue(customDescription.optional()); // 커스텀 값 확인
        assertTrue(customDescription.ignore()); // 커스텀 값 확인
    }

}