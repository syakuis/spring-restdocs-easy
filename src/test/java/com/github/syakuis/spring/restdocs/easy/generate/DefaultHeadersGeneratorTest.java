package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 */
@ExtendWith(MockitoExtension.class)
class DefaultHeadersGeneratorTest {
    @Mock
    private MessageSource messageSource;

    @Test
    void testHeadersGenerator() {
        DefaultRestDocs restDocs = new DefaultRestDocs(messageSource);
        HeadersGenerator headerGenerator = restDocs.headers();

        headerGenerator.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        headerGenerator.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML);

        RestDocs.Operator operator = headerGenerator.generate();
        List<Descriptor> descriptors = operator.toList();

        assertFalse(descriptors.isEmpty());
        assertEquals(2, descriptors.size());

        Descriptor contentTypeDescriptor = descriptors.getFirst();
        assertEquals(HttpHeaders.CONTENT_TYPE, contentTypeDescriptor.name());
        assertEquals(MediaType.APPLICATION_JSON.toString(), contentTypeDescriptor.description());

        Descriptor acceptDescriptor = descriptors.get(1);
        assertEquals(HttpHeaders.ACCEPT, acceptDescriptor.name());
        assertEquals(MediaType.APPLICATION_XML.toString(), acceptDescriptor.description());
    }
}