package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;

import java.util.List;

/**
 * Interface for generating and managing HTTP headers for API documentation.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 */
public interface HeadersGenerator {
    /**
     * Adds a header with its corresponding media type.
     *
     * @param httpHeaders the name of the HTTP header
     * @param mediaType the media type associated with the header
     * @return this HeaderGenerator instance for method chaining
     */
    HeadersGenerator add(String httpHeaders, MediaType mediaType);
    HeadersGenerator add(String httpHeaders, String description);

    RequestHeadersSnippet requestHeaders();
    ResponseHeadersSnippet responseHeaders();
    List<HeaderDescriptor> toHeader();

    /**
     * Generates an Operator containing all added headers.
     *
     * @return an Operator instance with the header information
     */
    RestDocs.Operator generate();
}
