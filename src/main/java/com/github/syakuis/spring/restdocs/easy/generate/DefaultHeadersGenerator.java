package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the HeaderGenerator interface.
 * This class is responsible for generating header information compatible with Spring REST Docs' HeaderDocumentation.
 * It allows adding headers with their corresponding media types and generates descriptors for API documentation.
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

    @Override
    public HeadersGenerator add(String httpHeaders, String description) {
        validHeaderName(httpHeaders);

        descriptors.put(httpHeaders, description);
        return this;
    }

    @Override
    public RequestHeadersSnippet requestHeaders() {
        return generate().requestHeaders();
    }

    @Override
    public ResponseHeadersSnippet responseHeaders() {
        return generate().responseHeaders();
    }

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
