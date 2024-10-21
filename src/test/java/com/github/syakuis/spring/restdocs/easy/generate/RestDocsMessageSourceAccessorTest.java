package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.DataClassLoader;
import com.github.syakuis.spring.restdocs.easy.core.DataClassMetadata;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-21
 */
@Slf4j
@WebMvcTest
@Import({MessageSourceAutoConfiguration.class})
class RestDocsMessageSourceAccessorTest {

    @Autowired
    private MessageSource messageSource;

    private RestDocsMessageSourceAccessor restDocsMessageSourceAccessor;

    @BeforeEach
    void init() {
        this.restDocsMessageSourceAccessor = new RestDocsMessageSourceAccessor(messageSource);
    }

    @Test
    void getMessage() {
        var dataClassLoader = DataClassLoader.of(Sample.class);
        var dataClassMetadata = dataClassLoader.toList();

        dataClassMetadata.forEach(it -> {
            var message = restDocsMessageSourceAccessor.getMessage(it);

            if (Objects.equals("name", it.fieldName())) {
                assertEquals("테스트", message);
            } else if (Objects.equals("content", it.fieldName())) {
                assertEquals("content", message);
            } else if (Objects.equals("abc", it.fieldName())) {
                String text = """
                    abc

                    A +\s
                    B +\s
                    C""";
                assertEquals(text, message);
            } else if (Objects.equals("sex", it.fieldName())) {
                String text = """
                    성별

                    male : 남성 +\s
                    female : 여성""";
                assertEquals(text, message);
            }
        });
    }

    record Sample(String name, String content, Abc abc, Sex sex) {
    }

    enum Abc {
        A, B, C
    }

    enum Sex {
        male, female
    }
}