package com.github.syakuis.spring.restdocs.easy.configuration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@Import({MessageSourceAutoConfiguration.class, RestDocsGenerationConfiguration.class})
@PropertyMapping("spring.test.restdocs")
public @interface AutoConfigureMvcRestDocs {
    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "outputDir")
    String outputDir() default "";

    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "uriScheme")
    String uriScheme() default "http";

    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "uriHost")
    String uriHost() default "localhost";

    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "uriPort")
    int uriPort() default 8080;
}
