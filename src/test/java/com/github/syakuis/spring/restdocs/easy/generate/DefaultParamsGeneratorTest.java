package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 */
class DefaultParamsGeneratorTest {
    @Test
    void testAddWithNameAndDescription() {
        ParamsGenerator generator = new DefaultParamsGenerator();
        generator.add("param1", "Description 1");
        RestDocs.Operator operator = generator.generate();

        List<Descriptor> descriptors = operator.toList();
        assertEquals(1, descriptors.size());
        assertEquals("param1", descriptors.getFirst().name());
        assertEquals("Description 1", descriptors.getFirst().description());
        assertEquals(JsonFieldType.STRING, descriptors.getFirst().type());
        assertFalse(descriptors.getFirst().optional());
    }

    @Test
    void testAddWithNameDescriptionAndType() {
        ParamsGenerator generator = new DefaultParamsGenerator();
        generator.add("param2", "Description 2", JsonFieldType.NUMBER);
        RestDocs.Operator operator = generator.generate();

        List<Descriptor> descriptors = operator.toList();
        assertEquals(1, descriptors.size());
        assertEquals("param2", descriptors.getFirst().name());
        assertEquals("Description 2", descriptors.getFirst().description());
        assertEquals(JsonFieldType.NUMBER, descriptors.getFirst().type());
        assertFalse(descriptors.getFirst().optional());
    }

    @Test
    void testAddWithAllParameters() {
        ParamsGenerator generator = new DefaultParamsGenerator();
        generator.add("param3", "Description 3", JsonFieldType.BOOLEAN, true);
        RestDocs.Operator operator = generator.generate();

        List<Descriptor> descriptors = operator.toList();
        assertEquals(1, descriptors.size());
        assertEquals("param3", descriptors.getFirst().name());
        assertEquals("Description 3", descriptors.getFirst().description());
        assertEquals(JsonFieldType.BOOLEAN, descriptors.getFirst().type());
        assertTrue(descriptors.getFirst().optional());
    }

    @Test
    void testAddDescriptor() {
        ParamsGenerator generator = new DefaultParamsGenerator();
        Descriptor descriptor = Descriptor.builder()
            .name("param4")
            .description("Description 4")
            .type(JsonFieldType.ARRAY)
            .optional(true)
            .build();
        generator.add(descriptor);
        RestDocs.Operator operator = generator.generate();

        List<Descriptor> descriptors = operator.toList();
        assertEquals(1, descriptors.size());
        assertEquals("param4", descriptors.getFirst().name());
        assertEquals("Description 4", descriptors.getFirst().description());
        assertEquals(JsonFieldType.ARRAY, descriptors.getFirst().type());
        assertTrue(descriptors.getFirst().optional());
    }

    @Test
    void testInvalidParameterName() {
        ParamsGenerator generator = new DefaultParamsGenerator();
        assertThrows(IllegalArgumentException.class, () -> {
            generator.add("", "Description");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            generator.add(null, "Description");
        });
    }
}