package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.FormParametersSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;

import java.util.List;

/**
 * Interface for generating parameter documentation in "Spring REST Docs Easy".
 * Provides methods to create descriptors for documenting request parameters, path variables,
 * and form parameters. This interface is part of "Spring REST Docs Easy", which extends
 * Spring REST Docs to provide enhanced documentation capabilities.
 *
 * <p>Features:</p>
 * - Supports path, query, and form parameters
 * - Configurable parameter types and optionality
 * - Supports message source integration for i18n
 * - Generates Spring REST Docs parameter snippets
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ParamsGenerator params = new DefaultParamsGenerator(messageSource)
 *     .add("userId", "{api.userId.description}", JsonFieldType.NUMBER)
 *     .add("email", "{api.email.description}", JsonFieldType.STRING, true);
 *
 * // Generate documentation
 * RestDocs.Operator operator = params.generate();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 *
 * @see org.springframework.restdocs.request.RequestDocumentation
 */
public interface ParamsGenerator {
    /**
     * Creates a descriptor for a parameter with default STRING type.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param name the name of the parameter (e.g., "userId", "page")
     * @param description the description or message key (e.g., "User ID", "{api.userId.description}")
     * @return this generator instance for method chaining
     */
    ParamsGenerator add(String name, String description);

    /**
     * Creates a descriptor with specified field type.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param name the name of the parameter (e.g., "count", "price")
     * @param description the description or message key
     * @param type the parameter type (e.g., NUMBER for integers, STRING for text)
     * @return this generator instance for method chaining
     */
    ParamsGenerator add(String name, String description, JsonFieldType type);

    /**
     * Creates a descriptor with complete specifications.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param name the name of the parameter
     * @param description the description or message key
     * @param type the parameter type
     * @param optional true if parameter is optional, false if required
     * @return this generator instance for method chaining
     */
    ParamsGenerator add(String name, String description, JsonFieldType type, boolean optional);

    /**
     * Adds a pre-built descriptor for more complex parameter documentation needs.
     *
     * @param descriptor the pre-configured descriptor
     * @return this generator instance for method chaining
     */
    ParamsGenerator add(Descriptor descriptor);

    /**
     * Creates a Spring REST Docs snippet for path parameters documentation.
     * Used for documenting URL path variables (e.g., /api/users/{id}).
     *
     * <p>Example usage in test:</p>
     * <pre>{@code
     * mockMvc.perform(get("/api/users/{id}", 123))
     *     .andDo(document("user-get",
     *         pathParameters()  // Documents {id} parameter
     *     ));
     * }</pre>
     *
     * @return snippet for path parameters documentation
     */
    PathParametersSnippet pathParameters();

    /**
     * Creates a Spring REST Docs snippet for query parameters documentation.
     * Used for documenting URL query parameters (e.g., /api/users?page=1&size=10).
     *
     * <p>Example usage in test:</p>
     * <pre>{@code
     * mockMvc.perform(get("/api/users")
     *         .param("page", "1")
     *         .param("size", "10"))
     *     .andDo(document("user-list",
     *         queryParameters()  // Documents page and size parameters
     *     ));
     * }</pre>
     *
     * @return snippet for query parameters documentation
     */
    QueryParametersSnippet queryParameters();

    /**
     * Creates a Spring REST Docs snippet for form parameters documentation.
     * Used for documenting form data in POST requests.
     *
     * <p>Example usage in test:</p>
     * <pre>{@code
     * mockMvc.perform(post("/api/users")
     *         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
     *         .param("username", "john")
     *         .param("email", "john@example.com"))
     *     .andDo(document("user-create",
     *         formParameters()  // Documents form parameters
     *     ));
     * }</pre>
     *
     * @return snippet for form parameters documentation
     */
    FormParametersSnippet formParameters();

    /**
     * Converts all defined parameters to Spring REST Docs ParameterDescriptor objects.
     * Useful when you need to customize the parameter documentation further.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<ParameterDescriptor> descriptors = generator.toParameter();
     * descriptors.forEach(descriptor -> {
     *     // Custom processing of parameter descriptors
     * });
     * }</pre>
     *
     * @return list of configured parameter descriptors
     */
    List<ParameterDescriptor> toParameter();

    /**
     * Creates an operator containing all parameter documentation.
     * The operator can be used to generate various types of parameter documentation.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * RestDocs.Operator operator = generator.generate();
     * // Use operator to generate different types of documentation
     * operator.pathParameters()
     *        .queryParameters()
     *        .formParameters();
     * }</pre>
     *
     * @return operator configured with all parameter descriptors
     */
    RestDocs.Operator generate();
}
