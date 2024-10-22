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

        // request snippet
        PathParametersSnippet pathParameters();
        QueryParametersSnippet queryParameters();
        FormParametersSnippet formParameters();
        RequestPartsSnippet requestParts();

        // payload snippet
        RequestFieldsSnippet requestFields();
        RequestPartFieldsSnippet requestPartFields(String path);
        ResponseFieldsSnippet responseFields();
        RequestBodySnippet requestBody();
        ResponseBodySnippet responseBody();
        RequestPartBodySnippet requestPartBody();

        // hypermedia snippet
        LinksSnippet links();

        // header snippet
        RequestHeadersSnippet requestHeaders();
        ResponseHeadersSnippet responseHeaders();

        // cookie snippet
        RequestCookiesSnippet requestCookies();
        ResponseCookiesSnippet responseCookies();
    }
}