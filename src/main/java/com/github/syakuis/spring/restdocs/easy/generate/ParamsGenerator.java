package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.FormParametersSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;

import java.util.List;

/**
 * Interface for generating descriptors that will be passed to Spring REST Docs' RequestDocumentation.
 * Creates descriptors for documenting request parameters, path variables, and request parts
 * through RequestDocumentation class.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 * @see org.springframework.restdocs.request.RequestDocumentation
 */
public interface ParamsGenerator {
    /**
     * Creates a descriptor for a parameter with default STRING type.
     * This descriptor will be passed to RequestDocumentation's methods
     * for API documentation generation.
     *
     * @param name The name of the parameter to be documented
     * @param description The description of the parameter
     * @return This generator instance for method chaining
     */
    ParamsGenerator add(String name, String description);

    /**
     * Creates a descriptor with specified field type.
     * This descriptor will be passed to RequestDocumentation's methods
     * for API documentation generation.
     *
     * @param name The name of the parameter to be documented
     * @param description The description of the parameter
     * @param type The field type for the parameter documentation
     * @return This generator instance for method chaining
     */
    ParamsGenerator add(String name, String description, JsonFieldType type);

    /**
     * Creates a descriptor with complete specifications.
     * This descriptor will be passed to RequestDocumentation's methods
     * for API documentation generation.
     *
     * @param name The name of the parameter to be documented
     * @param description The description of the parameter
     * @param type The field type for the parameter documentation
     * @param optional Whether the parameter is optional
     * @return This generator instance for method chaining
     */
    ParamsGenerator add(String name, String description, JsonFieldType type, boolean optional);

    /**
     * Adds a pre-built descriptor to be passed to RequestDocumentation.
     *
     * @param descriptor The pre-built descriptor
     * @return This generator instance for method chaining
     */
    ParamsGenerator add(Descriptor descriptor);

    PathParametersSnippet pathParameters();
    QueryParametersSnippet queryParameters();
    FormParametersSnippet formParameters();
    List<ParameterDescriptor> toParameter();

    /**
     * Creates an operator containing all descriptors to be passed to RequestDocumentation.
     *
     * @return An operator containing the descriptors for RequestDocumentation
     */
    RestDocs.Operator generate();
}
