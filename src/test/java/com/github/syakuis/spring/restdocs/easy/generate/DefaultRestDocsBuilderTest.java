package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-24
 */

@ExtendWith(MockitoExtension.class)
class DefaultRestDocsBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Test
    void testBuildWithMessageSource() {
        // Given
        RestDocsBuilder builder = new DefaultRestDocsBuilder();

        // When
        RestDocs restDocs = builder.messageSource(messageSource).build();

        // Then
        assertNotNull(restDocs);
        assertInstanceOf(DefaultRestDocs.class, restDocs);
    }

    @Test
    void testBuildWithJsonFieldTypeMapper() {
        // Given
        RestDocsBuilder builder = new DefaultRestDocsBuilder();

        // When
        RestDocs restDocs = builder.configure(mapper -> {
            mapper.add(String.class, JsonFieldType.STRING);
            mapper.add(Integer.class, JsonFieldType.NUMBER);
        }).build();

        // Then
        assertNotNull(restDocs);
        assertInstanceOf(DefaultRestDocs.class, restDocs);
    }

    @Test
    void testBuildWithMessageSourceAndJsonFieldTypeMapper() {
        // Given
        RestDocsBuilder builder = new DefaultRestDocsBuilder();

        // When
        RestDocs restDocs = builder
            .messageSource(messageSource)
            .configure(mapper -> {
                mapper.add(String.class, JsonFieldType.STRING);
                mapper.add(Integer.class, JsonFieldType.NUMBER);
            })
            .build();

        // Then
        assertNotNull(restDocs);
        assertInstanceOf(DefaultRestDocs.class, restDocs);
    }

    @Test
    void testJsonFieldTypeMapperConfiguration() {
        // Given
        RestDocsBuilder builder = new DefaultRestDocsBuilder();

        // When
        RestDocs restDocs = builder.configure(mapper -> {
            mapper.add(String.class, JsonFieldType.STRING);
            mapper.add(Integer.class, JsonFieldType.NUMBER);
            mapper.remove(String.class);

            Map<Class<?>, JsonFieldType> additionalData = new HashMap<>();
            additionalData.put(Long.class, JsonFieldType.NUMBER);
            additionalData.put(Double.class, JsonFieldType.NUMBER);
            mapper.addAll(additionalData);

            mapper.removeAll(additionalData.keySet());
        }).build();

        // Then
        assertNotNull(restDocs);
        assertInstanceOf(DefaultRestDocs.class, restDocs);
    }
}