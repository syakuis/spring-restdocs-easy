package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.FormParametersSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation of ParamsGenerator for "Spring REST Docs Easy".
 * Manages parameter documentation with support for message internationalization
 * and Spring REST Docs integration. This implementation extends Spring REST Docs
 * to provide enhanced parameter documentation capabilities.
 *
 * <p>Key features:</p>
 * - Maintains parameter descriptors in a HashSet for uniqueness
 * - Supports message source integration for i18n
 * - Validates parameter names
 * - Provides flexible parameter type configuration
 * - Generates Spring REST Docs compatible descriptors
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * DefaultParamsGenerator generator = new DefaultParamsGenerator(messageSource);
 * generator
 *     .add("userId", "{api.userId.description}", JsonFieldType.NUMBER)
 *     .add("email", "{api.email.description}", JsonFieldType.STRING, true)
 *     .add("role", "User role", JsonFieldType.STRING);
 *
 * // Generate documentation snippets
 * PathParametersSnippet pathSnippet = generator.pathParameters();
 * QueryParametersSnippet querySnippet = generator.queryParameters();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 *
 * @see org.springframework.restdocs.request.RequestDocumentation
 */
// todo descriptor cache
public class DefaultParamsGenerator extends DescriptionMessageSource implements ParamsGenerator {
    private final Set<Descriptor> descriptors = new HashSet<>();

    public DefaultParamsGenerator(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Validates the parameter name before creating a descriptor.
     *
     * @param paramName The parameter name to validate
     * @throws IllegalArgumentException if the parameter name is null or blank
     */
    private void validParamName(String paramName) {
        if (paramName == null || paramName.isBlank()) {
            throw new IllegalArgumentException("paramName must not be null or blank");
        }
    }

    /**
     * Creates a descriptor with default STRING type to be passed to RequestDocumentation.
     *
     * @param name The name of the parameter to be documented
     * @param description The description of the parameter
     * @return This generator instance for method chaining
     */
    @Override
    public ParamsGenerator add(String name, String description) {
        add(name, description, JsonFieldType.STRING, false);
        return this;
    }

    /**
     * Creates a descriptor with specified type to be passed to RequestDocumentation.
     *
     * @param name The name of the parameter to be documented
     * @param description The description of the parameter
     * @param type The field type for the parameter documentation
     * @return This generator instance for method chaining
     */
    @Override
    public ParamsGenerator add(String name, String description, JsonFieldType type) {
        add(name, description, type, false);
        return this;
    }

    /**
     * Creates a fully specified descriptor to be passed to RequestDocumentation.
     *
     * @param name The name of the parameter to be documented
     * @param description The description of the parameter
     * @param type The field type for the parameter documentation
     * @param optional Whether the parameter is optional
     * @return This generator instance for method chaining
     */
    @Override
    public ParamsGenerator add(String name, String description, JsonFieldType type, boolean optional) {
        validParamName(name);
        descriptors.add(Descriptor.builder()
            .name(name)
            .description(description)
            .type(type)
            .optional(optional).build());
        return this;
    }

    /**
     * Adds a pre-built descriptor to be passed to RequestDocumentation.
     *
     * @param descriptor The pre-built descriptor
     * @return This generator instance for method chaining
     */
    @Override
    public ParamsGenerator add(Descriptor descriptor) {
        descriptors.add(descriptor);
        return this;
    }

    /**
     * Creates a Spring REST Docs snippet for path parameters documentation.
     * Used for documenting URI path variables (e.g., /api/users/{id}).
     *
     * @return snippet for documenting path parameters
     * @see org.springframework.restdocs.request.RequestDocumentation#pathParameters
     */
    @Override
    public PathParametersSnippet pathParameters() {
        return generate().pathParameters();
    }

    /**
     * Creates a Spring REST Docs snippet for query parameters documentation.
     * Used for documenting URL query parameters (e.g., {@code /api/users?page=1&size=10}).
     *
     * @return snippet for documenting query parameters
     * @see org.springframework.restdocs.request.RequestDocumentation#queryParameters
     */
    @Override
    public QueryParametersSnippet queryParameters() {
        return generate().queryParameters();
    }

    /**
     * Creates a Spring REST Docs snippet for form parameters documentation.
     * Used for documenting form data in POST requests with
     * application/x-www-form-urlencoded content type.
     *
     * @return snippet for documenting form parameters
     * @see org.springframework.restdocs.request.RequestDocumentation#formParameters
     */
    @Override
    public FormParametersSnippet formParameters() {
        return generate().formParameters();
    }

    /**
     * Converts all defined parameters to Spring REST Docs ParameterDescriptor objects.
     * This method resolves any message keys in the descriptions before conversion.
     *
     * @return list of configured ParameterDescriptor objects
     * @see org.springframework.restdocs.request.ParameterDescriptor
     */
    @Override
    public List<ParameterDescriptor> toParameter() {
        return generate().toParameter();
    }

    /**
     * Creates an operator containing all parameter descriptors.
     * Resolves message keys in descriptions and prepares descriptors
     * for documentation generation.
     *
     * @return an operator configured with all parameter descriptors
     */
    @Override
    public RestDocs.Operator generate() {
        return new DefaultRestDocs.DefaultOperator(descriptors.stream().map(it -> it.description(getMessageByExpression(it.description()))).toList());
    }
}
