package io.github.syakuis.spring.restdocs.easy.generate;

import io.github.syakuis.spring.restdocs.easy.core.ClassMetadataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-21
 */
@Slf4j
@WebMvcTest
@Import({MessageSourceAutoConfiguration.class})
class DescriptionMessageSourceTest {

    @Autowired
    private MessageSource messageSource;

    private DescriptionMessageSource descriptionMessageSource;

    @BeforeEach
    void init() {
        this.descriptionMessageSource = new DescriptionMessageSource(messageSource);
    }

    @Test
    void getMessage() {
        var dataClassLoader = ClassMetadataGenerator.of(Sample.class);
        var dataClassMetadata = dataClassLoader.toList();

        dataClassMetadata.forEach(it -> {
            var message = descriptionMessageSource.getMessage(it);

            if (Objects.equals("name", it.name())) {
                assertEquals("테스트", message);
            } else if (Objects.equals("content", it.name())) {
                assertEquals("content", message);
            } else if (Objects.equals("abc", it.name())) {
                String text = """
                    abc

                    A +\s
                    B +\s
                    C""";
                assertEquals(text, message);
            } else if (Objects.equals("sex", it.name())) {
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