package com.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-21
 */
@WebMvcTest
@Import({MessageSourceAutoConfiguration.class})
class ClassLoaderRestDocsTest {
    @Autowired
    private MessageSource messageSource;

    private ClassLoaderRestDocs classLoaderRestDocs;

    @BeforeEach
    void init() {
        this.classLoaderRestDocs = new ClassLoaderRestDocs(messageSource, Collections.emptyList());
    }

    @Test
    void getMessage() {
        var result = classLoaderRestDocs.toList(Sample.class);

        result.stream().filter(it -> it.name().equals("name")).forEach(it -> {
            assertEquals("테스트", it.description());
        });
    }

    record Sample(String name) {
    }
}