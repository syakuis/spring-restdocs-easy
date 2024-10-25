package io.github.syakuis.spring.restdocs.easy.configuration;

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
 * Annotation for configuring "Spring REST Docs Easy" in Spring MVC test environment.
 * Provides automatic configuration for REST Docs with message source support and
 * customizable documentation settings.
 *
 * <p>Features:</p>
 * - Configures Spring REST Docs with MockMvc support
 * - Enables message source auto-configuration for i18n
 * - Provides customizable URI scheme, host, and port
 * - Integrates with "Spring REST Docs Easy" configuration
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @AutoConfigureMvcRestDocs(
 *     outputDir = "build/generated-snippets",
 *     uriScheme = "https",
 *     uriHost = "api.example.com",
 *     uriPort = 443
 * )
 * @WebMvcTest(UserController.class)
 * class UserControllerTest {
 *     // Test methods
 * }
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 * @see org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
 * @see RestDocsEasyConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@Import({MessageSourceAutoConfiguration.class, RestDocsEasyConfiguration.class})
@PropertyMapping("spring.test.restdocs")
public @interface AutoConfigureMvcRestDocs {

    /**
     * The output directory where generated documentation snippets will be written.
     * Alias for {@link AutoConfigureRestDocs#value()}.
     *
     * @return the output directory path
     */
    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "value")
    String value() default "";

    /**
     * The output directory where generated documentation snippets will be written.
     * Alias for {@link AutoConfigureRestDocs#outputDir()}.
     *
     * @return the output directory path
     */
    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "outputDir")
    String outputDir() default "";

    /**
     * The scheme for documented URIs (e.g., "http" or "https").
     * Alias for {@link AutoConfigureRestDocs#uriScheme()}.
     *
     * @return the URI scheme
     */
    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "uriScheme")
    String uriScheme() default "http";

    /**
     * The host for documented URIs (e.g., "localhost" or "api.example.com").
     * Alias for {@link AutoConfigureRestDocs#uriHost()}.
     *
     * @return the URI host
     */
    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "uriHost")
    String uriHost() default "localhost";

    /**
     * The port for documented URIs.
     * Alias for {@link AutoConfigureRestDocs#uriPort()}.
     *
     * @return the URI port
     */
    @AliasFor(annotation = AutoConfigureRestDocs.class, attribute = "uriPort")
    int uriPort() default 8080;
}
