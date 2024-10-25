package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;

import java.util.List;

/**
 * Interface for generating and managing HTTP headers documentation in "Spring REST Docs Easy".
 * Provides methods to define request and response headers with their descriptions
 * and media types for API documentation. This interface is part of "Spring REST Docs Easy",
 * which extends Spring REST Docs to provide enhanced documentation capabilities.
 *
 * <p>Features:</p>
 * - Supports standard HTTP headers and custom headers
 * - Handles media type descriptions
 * - Generates Spring REST Docs header snippets
 * - Supports message source integration for i18n
 * - Simplifies header documentation process
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * HeadersGenerator headers = new DefaultHeadersGenerator(messageSource)
 *     .add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
 *     .add(HttpHeaders.AUTHORIZATION, "{api.auth.description}");
 *
 * // Generate documentation
 * RestDocs.Operator operator = headers.generate();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 * @see org.springframework.restdocs.headers.HeaderDocumentation
 */
public interface HeadersGenerator {

    /**
     * Adds a header with its corresponding media type to the documentation.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * generator.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     *         .add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML);
     * }</pre>
     *
     * @param httpHeaders the name of the HTTP header (e.g., "Content-Type", "Accept")
     * @param mediaType the media type associated with the header
     * @return this generator instance for method chaining
     */
    HeadersGenerator add(String httpHeaders, MediaType mediaType);

    /**
     * Adds a header with a custom description to the documentation.
     * The description can be a direct string or a message key in the format {key}.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * generator.add(HttpHeaders.AUTHORIZATION, "{api.auth.description}")
     *         .add("Custom-Header", "Custom header for tracking");
     * }</pre>
     *
     * @param httpHeaders the name of the HTTP header
     * @param description the description or message key for the header
     * @return this generator instance for method chaining
     */
    HeadersGenerator add(String httpHeaders, String description);

    /**
     * Creates a Spring REST Docs snippet for request headers documentation.
     * Used to document headers that should be included in API requests.
     *
     * <p>Example usage in test:</p>
     * <pre>{@code
     * mockMvc.perform(get("/api/resource"))
     *     .andDo(document("endpoint-name",
     *         generator.requestHeaders()
     *     ));
     * }</pre>
     *
     * @return snippet containing all defined request headers
     */
    RequestHeadersSnippet requestHeaders();

    /**
     * Creates a Spring REST Docs snippet for response headers documentation.
     * Used to document headers that will be included in API responses.
     *
     * <p>Example usage in test:</p>
     * <pre>{@code
     * mockMvc.perform(get("/api/resource"))
     *     .andDo(document("endpoint-name",
     *         generator.responseHeaders()
     *     ));
     * }</pre>
     *
     * @return snippet containing all defined response headers
     */
    ResponseHeadersSnippet responseHeaders();

    /**
     * Converts all defined headers to Spring REST Docs HeaderDescriptor objects.
     * Useful when you need to customize the header documentation further.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<HeaderDescriptor> descriptors = generator.toHeader();
     * descriptors.forEach(descriptor -> {
     *     // Custom processing of header descriptors
     * });
     * }</pre>
     *
     * @return list of configured HeaderDescriptor objects
     */
    List<HeaderDescriptor> toHeader();

    /**
     * Generates a RestDocs.Operator containing all header documentation.
     * This operator can be used to generate the final API documentation
     * and combine with other documentation elements.
     *
     * @return operator configured with all defined headers
     */
    RestDocs.Operator generate();
}
