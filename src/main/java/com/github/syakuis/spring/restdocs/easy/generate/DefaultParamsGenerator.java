package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.FormParametersSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default implementation for creating descriptors to be passed to Spring REST Docs' RequestDocumentation.
 * Generates descriptors that RequestDocumentation uses to create API documentation
 * for request parameters, path variables, and request parts.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 * @see org.springframework.restdocs.request.RequestDocumentation
 */
public class DefaultParamsGenerator implements ParamsGenerator {
    private final Set<Descriptor> params = new HashSet<>();

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
     * @throws IllegalArgumentException if name is null or blank
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
     * @throws IllegalArgumentException if name is null or blank
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
     * @throws IllegalArgumentException if name is null or blank
     */
    @Override
    public ParamsGenerator add(String name, String description, JsonFieldType type, boolean optional) {
        validParamName(name);
        params.add(Descriptor.builder()
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
        params.add(descriptor);
        return this;
    }

    @Override
    public PathParametersSnippet pathParameters() {
        return generate().pathParameters();
    }

    @Override
    public QueryParametersSnippet queryParameters() {
        return generate().queryParameters();
    }

    @Override
    public FormParametersSnippet formParameters() {
        return generate().formParameters();
    }

    @Override
    public List<ParameterDescriptor> toParameter() {
        return generate().toParameter();
    }

    /**
     * Creates an operator containing all descriptors to be passed to RequestDocumentation.
     *
     * @return An operator containing the descriptors for RequestDocumentation
     */
    @Override
    public RestDocs.Operator generate() {
        return new DefaultRestDocs.DefaultOperator(params.stream().toList());
    }
}
