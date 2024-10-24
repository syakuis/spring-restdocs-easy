package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
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
 * Defines the RestDocs API for generating documentation descriptors.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
public interface RestDocs {
    /**
     * Creates a new HeaderGenerator instance.
     *
     * @return a new HeaderGenerator for adding and managing HTTP headers
     */
    HeadersGenerator headers();
    ParamsGenerator params();
    DescriptorsGenerator descriptors();
    Operator generate(Class<?> targetClass);

    /**
     * Builder class for constructing RestDocs instances.
     */
    class RestDocsBuilder implements RestDocs.Builder {
        private MessageSource messageSource;

        @Override
        public RestDocs.Builder messageSource(MessageSource messageSource) {
            this.messageSource = messageSource;
            return this;
        }

        @Override
        public RestDocs build() {
            return new DefaultRestDocs(messageSource);
        }
    }

    /**
     * Provides a builder for creating RestDocs instances.
     *
     * @return a RestDocs.Builder instance
     */
    static RestDocs.Builder builder() {
        return new RestDocsBuilder();
    }

    interface Builder {
        RestDocs.Builder messageSource(MessageSource messageSource);
        RestDocs build();
    }

    interface Operator {
        Operator addAll(Descriptor... descriptor);
        Operator filter(String... fieldName);
        Operator exclude(String... fieldName);
        Operator optional(String... fieldName);
        Operator require(String... fieldName);
        Operator ignore(String... fieldName);
        Operator notIgnore(String... fieldName);
        List<FieldDescriptor> toField();
        List<SubsectionDescriptor> toSubsection();
        List<RequestPartDescriptor> toRequestPart();
        List<ParameterDescriptor> toParameter();
        List<LinkDescriptor> toLink();
        List<HeaderDescriptor> toHeader();
        List<CookieDescriptor> toCookie();
        List<Descriptor> toList();
        String join();
        String join(CharSequence delimiter);
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
         * Creates a snippet for documenting JSON response body fields with a prefix.
         *
         * @param prefix The prefix to be applied to all field paths
         * @return A snippet for response body fields documentation
         * @see org.springframework.restdocs.payload.PayloadDocumentation#responseFields
         */
        ResponseFieldsSnippet responseFields(String prefix);

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