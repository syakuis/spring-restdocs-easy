package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
class DescriptorCollectorTest {

    @Test
    void merge_withEmptyTargetAndSource_returnsEmptyList() {
        List<Descriptor> target = Collections.emptyList();
        List<Descriptor> source = Collections.emptyList();

        List<Descriptor> result = DescriptorCollector.merge(target, source);

        assertTrue(result.isEmpty());
    }

    @Test
    void merge_withEmptySource_returnsTargetList() {
        List<Descriptor> target = Arrays.asList(
                Descriptor.builder().name("field1").description("desc1").build(),
                Descriptor.builder().name("field2").type(JsonFieldType.NUMBER).optional(true).build()
        );
        List<Descriptor> source = Collections.emptyList();

        List<Descriptor> result = DescriptorCollector.merge(target, source);

        assertEquals(target, result);
    }

    @Test
    void merge_withNonEmptyTargetAndSource_returnsMergedList() {
        List<Descriptor> target = Arrays.asList(
                Descriptor.builder().name("field1").description("desc1").build(),
                Descriptor.builder().name("field2").type(JsonFieldType.NUMBER).optional(true).build()
        );
        List<Descriptor> source = Arrays.asList(
                Descriptor.builder().name("field2").description("new_desc2").type(JsonFieldType.BOOLEAN).build(),
                Descriptor.builder().name("field3").description("desc3").ignore(true).build()
        );

        List<Descriptor> result = DescriptorCollector.merge(target, source);

        assertEquals(3, result.size());

        assertTrue(result.stream().anyMatch(d -> d.name().equals("field1") && d.description().equals("desc1")));

        assertTrue(result.stream().anyMatch(d -> d.name().equals("field2")
                && d.description().equals("new_desc2")
                && d.type() == JsonFieldType.BOOLEAN
                && !d.optional())); // Optional should be overwritten by source

        assertTrue(result.stream().anyMatch(d -> d.name().equals("field3")
                && d.description().equals("desc3")
                && d.ignore()));
    }

    @Test
    void merge_withDuplicateFieldsInSource_returnsListWithLatestValues() {
        List<Descriptor> target = Arrays.asList(
                Descriptor.builder().name("field1").description("desc1").build(),
                Descriptor.builder().name("field2").type(JsonFieldType.NUMBER).optional(true).build()
        );
        List<Descriptor> source = Arrays.asList(
                Descriptor.builder().name("field2").description("new_desc2").type(JsonFieldType.BOOLEAN).build(),
                Descriptor.builder().name("field2").description("latest_desc2").type(JsonFieldType.ARRAY).optional(false).build()
        );

        List<Descriptor> result = DescriptorCollector.merge(target, source);

        assertEquals(2, result.size());
        assertEquals("field1", result.get(0).name());
        assertEquals("desc1", result.get(0).description());
        assertEquals("field2", result.get(1).name());
        assertEquals("latest_desc2", result.get(1).description());
        assertEquals(JsonFieldType.ARRAY, result.get(1).type());
        assertFalse(result.get(1).optional());
    }

    @Test
    void merge_withNullSource_returnsTargetList() {
        List<Descriptor> target = Arrays.asList(
                Descriptor.builder().name("field1").description("desc1").build(),
                Descriptor.builder().name("field2").type(JsonFieldType.NUMBER).optional(true).build()
        );

        List<Descriptor> result = DescriptorCollector.merge(target, null);

        assertEquals(target, result);
    }
}