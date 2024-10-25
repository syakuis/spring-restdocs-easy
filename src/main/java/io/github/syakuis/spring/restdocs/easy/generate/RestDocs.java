package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.cookies.ResponseCookiesSnippet;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.request.*;

import java.util.List;

/**
 * Core interface of "Spring REST Docs Easy" for generating API documentation descriptors.
 * Provides comprehensive support for documenting various aspects of REST APIs including
 * headers, parameters, request/response bodies, cookies, and hypermedia links.
 *
 * <p>Features:</p>
 * - Headers and cookies documentation
 * - Request/response parameters documentation
 * - Request/response body documentation
 * - Path, query, and form parameters documentation
 * - Multipart request documentation
 * - Hypermedia links documentation
 * - Validation group support
 * - Prefix support for nested structures
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * RestDocs restDocs = RestDocs.builder()
 *     .messageSource(messageSource)
 *     .configure(mapper -> mapper.add(LocalDate.class, JsonFieldType.STRING))
 *     .build();
 *
 * Operator operator = restDocs.generate(UserDto.class);
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
// todo Caching the generated descriptor class.
public interface RestDocs {

    /**
     * Creates a new HeadersGenerator for HTTP header documentation.
     *
     * @return a new HeadersGenerator instance
     */
    HeadersGenerator headers();

    /**
     * Creates a new ParamsGenerator for request parameter documentation.
     *
     * @return a new ParamsGenerator instance
     */
    // todo validation and groups settings
    ParamsGenerator params();

    /**
     * Creates a new DescriptorsGenerator for general field documentation.
     *
     * @return a new DescriptorsGenerator instance
     */
    // todo validation and groups  settings
    DescriptorsGenerator descriptors();

    /**
     * Generates documentation descriptors for the specified class.
     *
     * @param targetClass the class to generate descriptors for
     * @return an Operator containing the generated descriptors
     */
    Operator generate(Class<?> targetClass);

    /**
     * Generates documentation descriptors with validation groups.
     *
     * @param targetClass the class to generate descriptors for
     * @param validGroups validation groups to consider
     * @return an Operator containing the generated descriptors
     */
    Operator generate(Class<?> targetClass, Class<?>... validGroups);

    /**
     * Generates documentation descriptors with a prefix for nested structures.
     *
     * @param prefix prefix to add to field paths (e.g., "user." for "user.name")
     * @param targetClass the class to generate descriptors for
     * @return an Operator containing the generated descriptors
     */
    Operator generate(String prefix, Class<?> targetClass);

    /**
     * Generates documentation descriptors with prefix and validation groups.
     *
     * @param prefix prefix to add to field paths
     * @param targetClass the class to generate descriptors for
     * @param validGroups validation groups to consider
     * @return an Operator containing the generated descriptors
     */
    Operator generate(String prefix, Class<?> targetClass, Class<?>... validGroups);

    /**
     * Provides a builder for creating RestDocs instances.
     *
     * @return a RestDocs.Builder instance
     */
    static RestDocsBuilder builder() {
        return new DefaultRestDocsBuilder();
    }

    /**
     * Operator interface for manipulating and generating documentation snippets.
     * Provides methods for filtering, modifying, and converting descriptors into
     * various Spring REST Docs snippet types.
     */
    interface Operator {

        /**
         * Adds all fields from the specified class with a prefix.
         *
         * @param prefix prefix for field paths
         * @param targetClass class to generate descriptors from
         * @param validGroups validation groups to consider
         * @return this operator instance
         */
        Operator addAll(String prefix, Class<?> targetClass, Class<?>... validGroups);

        /**
         * Adds all descriptors from the provided list.
         *
         * @param descriptor list of descriptors to add
         * @return this operator instance
         */
        Operator addAll(List<Descriptor> descriptor);

        /**
         * Adds the specified descriptors.
         *
         * @param descriptor descriptors to add
         * @return this operator instance
         */
        Operator addAll(Descriptor... descriptor);

        /**
         * Filters descriptors to include only the specified field names.
         *
         * @param fieldName field names to include
         * @return this operator instance
         */
        Operator filter(String... fieldName);

        /**
         * Excludes specified fields from documentation.
         *
         * @param fieldName field names to exclude
         * @return this operator instance
         */
        Operator exclude(String... fieldName);

        /**
         * Marks specified fields as optional.
         *
         * @param fieldName field names to mark as optional
         * @return this operator instance
         */
        Operator optional(String... fieldName);

        /**
         * Marks specified fields as required.
         *
         * @param fieldName field names to mark as required
         * @return this operator instance
         */
        Operator require(String... fieldName);

        /**
         * Marks specified fields to be ignored in documentation.
         *
         * @param fieldName field names to ignore
         * @return this operator instance
         */
        Operator ignore(String... fieldName);

        /**
         * Marks specified fields to not be ignored in documentation.
         *
         * @param fieldName field names to not ignore
         * @return this operator instance
         */
        Operator notIgnore(String... fieldName);

        /**
         * Converts descriptors to Spring REST Docs FieldDescriptor list.
         * Used for documenting JSON fields in request/response bodies.
         *
         * @return list of FieldDescriptor objects
         * @see org.springframework.restdocs.payload.FieldDescriptor
         */
        List<FieldDescriptor> toField();

        /**
         * Converts descriptors to Spring REST Docs SubsectionDescriptor list.
         * Used for documenting nested objects in JSON payloads.
         *
         * @return list of SubsectionDescriptor objects
         * @see org.springframework.restdocs.payload.SubsectionDescriptor
         */
        List<SubsectionDescriptor> toSubsection();

        /**
         * Converts descriptors to Spring REST Docs RequestPartDescriptor list.
         * Used for documenting parts in multipart/form-data requests.
         *
         * @return list of RequestPartDescriptor objects
         * @see org.springframework.restdocs.request.RequestPartDescriptor
         */
        List<RequestPartDescriptor> toRequestPart();

        /**
         * Converts descriptors to Spring REST Docs ParameterDescriptor list.
         * Used for documenting URL parameters (path, query, form).
         *
         * @return list of ParameterDescriptor objects
         * @see org.springframework.restdocs.request.ParameterDescriptor
         */
        List<ParameterDescriptor> toParameter();

        /**
         * Converts descriptors to Spring REST Docs LinkDescriptor list.
         * Used for documenting hypermedia links in HATEOAS responses.
         *
         * @return list of LinkDescriptor objects
         * @see org.springframework.restdocs.hypermedia.LinkDescriptor
         */
        List<LinkDescriptor> toLink();

        /**
         * Converts descriptors to Spring REST Docs HeaderDescriptor list.
         * Used for documenting HTTP headers in requests and responses.
         *
         * @return list of HeaderDescriptor objects
         * @see org.springframework.restdocs.headers.HeaderDescriptor
         */
        List<HeaderDescriptor> toHeader();

        /**
         * Converts descriptors to Spring REST Docs CookieDescriptor list.
         * Used for documenting cookies in requests and responses.
         *
         * @return list of CookieDescriptor objects
         * @see org.springframework.restdocs.cookies.CookieDescriptor
         */
        List<CookieDescriptor> toCookie();

        /**
         * Returns all descriptors as a list of "Spring REST Docs Easy" Descriptor objects.
         * Provides access to the raw descriptor data before conversion to specific types.
         *
         * @return list of Descriptor objects
         * @see Descriptor
         */
        List<Descriptor> toList();

        /**
         * Joins field paths into a single string.
         *
         * @return joined field paths
         */
        String join();

        /**
         * Joins field paths with a delimiter.
         *
         * @param delimiter delimiter to use between field paths
         * @return joined field paths
         */
        String join(CharSequence delimiter);

        /**
         * Joins field paths with delimiter and surrounding characters.
         *
         * @param delimiter delimiter between field paths
         * @param prefix prefix for the entire string
         * @param suffix suffix for the entire string
         * @return joined field paths
         */
        String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix);

        /**
         * Creates a snippet for documenting URL path parameters.
         * Example: {@code /api/users/{id} where {id}} is a path parameter
         *
         * @return A snippet for path parameters documentation
         * @see org.springframework.restdocs.request.RequestDocumentation#pathParameters
         */
        PathParametersSnippet pathParameters();

        /**
         * Creates a snippet for documenting URL query parameters.
         * Example: {@code /api/users?page=1&size=10} where page and size are query parameters
         *
         * @return A snippet for query parameters documentation
         * @see org.springframework.restdocs.request.RequestDocumentation#queryParameters
         */
        QueryParametersSnippet queryParameters();

        /**
         * Creates a snippet for documenting form parameters in request body.
         * Used when Content-Type is application/x-www-form-urlencoded
         *
         * @return A snippet for form parameters documentation
         * @see org.springframework.restdocs.request.RequestDocumentation#formParameters
         */
        FormParametersSnippet formParameters();

        /**
         * Creates a snippet for documenting multipart request parts.
         * Used when Content-Type is multipart/form-data
         *
         * @return A snippet for multipart request parts documentation
         * @see org.springframework.restdocs.request.RequestDocumentation#requestParts
         */
        RequestPartsSnippet requestParts();

        /**
         * Creates a snippet for documenting JSON request body fields.
         *
         * @return A snippet for request body fields documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#requestFields
         */
        RequestFieldsSnippet requestFields();

        /**
         * Creates a snippet for documenting fields within a specific part of a multipart request.
         *
         * @param path The path to the part within the multipart request
         * @return A snippet for request part fields documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#requestPartFields
         */
        RequestPartFieldsSnippet requestPartFields(String path);

        /**
         * Creates a snippet for documenting JSON response body fields.
         *
         * @return A snippet for response body fields documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#responseFields
         */
        ResponseFieldsSnippet responseFields();

        /**
         * Creates a snippet for documenting the raw request body.
         *
         * @return A snippet for request body documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#requestBody
         */
        RequestBodySnippet requestBody();

        /**
         * Creates a snippet for documenting the raw response body.
         *
         * @return A snippet for response body documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#responseBody
         */
        ResponseBodySnippet responseBody();

        /**
         * Creates a snippet for documenting the raw body of a multipart request part.
         *
         * @return A snippet for request part body documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#requestPartBody
         */
        RequestPartBodySnippet requestPartBody();

        /**
         * Creates a snippet for documenting hypermedia links in the response.
         * Used for HATEOAS-enabled endpoints
         *
         * @return A snippet for hypermedia links documentation
         * @see org.springframework.restdocs.hypermedia.HypermediaDocumentation#links
         */
        LinksSnippet links();

        /**
         * Creates a snippet for documenting HTTP request headers.
         *
         * @return A snippet for request headers documentation
         * @see org.springframework.restdocs.headers.HeaderDocumentation#requestHeaders
         */
        RequestHeadersSnippet requestHeaders();

        /**
         * Creates a snippet for documenting HTTP response headers.
         *
         * @return A snippet for response headers documentation
         * @see org.springframework.restdocs.headers.HeaderDocumentation#responseHeaders
         */
        ResponseHeadersSnippet responseHeaders();

        /**
         * Creates a snippet for documenting request cookies.
         *
         * @return A snippet for request cookies documentation
         * @see org.springframework.restdocs.cookies.CookieDocumentation#requestCookies
         */
        RequestCookiesSnippet requestCookies();

        /**
         * Creates a snippet for documenting response cookies.
         *
         * @return A snippet for response cookies documentation
         * @see org.springframework.restdocs.cookies.CookieDocumentation#responseCookies
         */
        ResponseCookiesSnippet responseCookies();
    }
}