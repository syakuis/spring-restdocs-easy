package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of HeadersGenerator for "Spring REST Docs Easy".
 * Manages HTTP header documentation with support for message internationalization
 * and Spring REST Docs integration. "Spring REST Docs Easy" extends Spring REST Docs
 * to provide enhanced documentation capabilities with easier configuration and usage.
 *
 * <p>Key features:</p>
 * - Maintains header descriptions in a LinkedHashMap for order preservation
 * - Supports message source integration for i18n
 * - Validates header names
 * - Converts MediaType to appropriate string representations
 * - Generates Spring REST Docs compatible descriptors
 * - Simplifies Spring REST Docs header documentation process
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * DefaultHeadersGenerator generator = new DefaultHeadersGenerator(messageSource);
 * generator
 *     .add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
 *     .add(HttpHeaders.AUTHORIZATION, "{api.auth.description}")
 *     .add("Custom-Header", "Custom header description");
 *
 * // Generate documentation snippets
 * RequestHeadersSnippet requestSnippet = generator.requestHeaders();
 * ResponseHeadersSnippet responseSnippet = generator.responseHeaders();
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 * @see org.springframework.restdocs.headers.HeaderDocumentation
 */
// todo descriptor cache
public class DefaultHeadersGenerator extends DescriptionMessageSource implements HeadersGenerator {
    private final Map<String, String> descriptors = new LinkedHashMap<>();

    public DefaultHeadersGenerator(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Validates the HTTP header name.
     *
     * @param headerName the header name to validate
     * @throws IllegalArgumentException if headerName is null or blank
     */
    private void validHeaderName(String headerName) {
        if (headerName == null || headerName.isBlank()) {
            throw new IllegalArgumentException("headerName must not be null or blank");
        }
    }

    /**
     * Adds a header with its corresponding media type to the descriptors map.
     * This method prepares the information needed to create a HeaderDescriptor using HeaderDocumentation.headerWithName().
     *
     * @param httpHeaders the header name, which can be any standard or custom HTTP header defined in Spring's HttpHeaders class.
     *                    HttpHeaders provides constants for commonly used headers like HttpHeaders.ACCEPT, HttpHeaders.CONTENT_TYPE, etc.
     * @param mediaType   the media type of the header, which should be a valid MediaType defined in Spring's MediaType class.
     *                    MediaType provides constants for common media types like MediaType.APPLICATION_JSON, MediaType.TEXT_XML, etc.
     * @return the current instance of DefaultHeaderGenerator
     * @see org.springframework.http.HttpHeaders
     * @see org.springframework.http.MediaType
     * @see org.springframework.restdocs.headers.HeaderDocumentation#headerWithName(String)
     */
    @Override
    public HeadersGenerator add(String httpHeaders, MediaType mediaType) {
        validHeaderName(httpHeaders);

        descriptors.put(httpHeaders, mediaType != null ? mediaType.toString() : null);
        return this;
    }

    /**
     * Adds a header with a custom description to the descriptors map.
     * The description can be a direct string or a message key in the format {key}.
     *
     * @param httpHeaders the header name (e.g., "Authorization", "Custom-Header")
     * @param description the header description or message key (e.g., "{api.auth.description}")
     * @return the current instance for method chaining
     * @see org.springframework.http.HttpHeaders
     */
    @Override
    public HeadersGenerator add(String httpHeaders, String description) {
        validHeaderName(httpHeaders);

        descriptors.put(httpHeaders, description);
        return this;
    }

    /**
     * Creates a Spring REST Docs snippet for request headers documentation.
     * Converts all added headers to RequestHeadersSnippet format.
     *
     * @return snippet containing all defined request headers
     * @see org.springframework.restdocs.headers.HeaderDocumentation#requestHeaders
     */
    @Override
    public RequestHeadersSnippet requestHeaders() {
        return generate().requestHeaders();
    }

    /**
     * Creates a Spring REST Docs snippet for response headers documentation.
     * Converts all added headers to ResponseHeadersSnippet format.
     *
     * @return snippet containing all defined response headers
     * @see org.springframework.restdocs.headers.HeaderDocumentation#responseHeaders
     */
    @Override
    public ResponseHeadersSnippet responseHeaders() {
        return generate().responseHeaders();
    }

    /**
     * Converts all defined headers to Spring REST Docs HeaderDescriptor objects.
     * This method resolves any message keys in the descriptions before conversion.
     *
     * @return list of HeaderDescriptor objects
     * @see org.springframework.restdocs.headers.HeaderDescriptor
     */
    @Override
    public List<HeaderDescriptor> toHeader() {
        return generate().toHeader();
    }

    /**
     * Generates a DefaultOperator with the added descriptors.
     * This method creates HeaderDescriptors that can be used with HeaderDocumentation.requestHeaders() or HeaderDocumentation.responseHeaders().
     *
     * @return a new instance of DefaultOperator with HeaderDescriptors created from the added headers
     * @see org.springframework.restdocs.headers.HeaderDocumentation#requestHeaders(org.springframework.restdocs.headers.HeaderDescriptor...)
     * @see org.springframework.restdocs.headers.HeaderDocumentation#responseHeaders(org.springframework.restdocs.headers.HeaderDescriptor...)
     */
    @Override
    public RestDocs.Operator generate() {
        return new DefaultRestDocs.DefaultOperator(descriptors.entrySet().stream()
            .map(entry -> Descriptor.builder()
                .name(entry.getKey())
                .description(getMessageByExpression(entry.getValue()))
                .build()).toList());
    }
}
