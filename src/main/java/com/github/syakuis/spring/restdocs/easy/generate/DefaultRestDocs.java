package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.CookieDocumentation;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.cookies.ResponseCookiesSnippet;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.request.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.syakuis.spring.restdocs.easy.generate.DescriptorCollector.*;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;

/**
 * Default implementation of the RestDocs interface.
 * This class generates documentation descriptors using the provided MessageSource and target class.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
class DefaultRestDocs implements RestDocs {
    private final MessageSource messageSource;

    public DefaultRestDocs(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public HeadersGenerator headers() {
        return new DefaultHeadersGenerator();
    }

    @Override
    public ParamsGenerator params() {
        return new DefaultParamsGenerator();
    }

    /**
     * Generates an Operator for the specified target class.
     *
     * @param targetClass the class to generate descriptors for
     * @return a new Operator instance
     */
    @Override
    public Operator generate(Class<?> targetClass) {
        return new DefaultOperator(new ClassLoaderDescriptor(messageSource, targetClass).generate());
    }

    /**
     * Default implementation of the Operator interface.
     * Handles operations like filtering, excluding, and modifying descriptors.
     */
    static class DefaultOperator implements Operator {
        private Stream<Descriptor> descriptors;

        public DefaultOperator(List<Descriptor> descriptors) {
            this.descriptors = descriptors.stream();
        }

        /**
         * Adds additional descriptors to the existing stream.
         *
         * @param descriptor the descriptors to add
         * @return the updated Operator instance
         */
        @Override
        public Operator addAll(Descriptor... descriptor) {
            this.descriptors = DescriptorCollector.merge(descriptors.toList(), Arrays.asList(descriptor)).stream();
            return this;
        }

        /**
         * Filters descriptors by matching field names.
         *
         * @param fieldName the field names to filter by
         * @return the updated Operator instance
         */
        @Override
        public Operator filter(String... fieldName) {
            Set<String> fieldSet = new HashSet<>(Arrays.asList(fieldName));
            this.descriptors = descriptors.filter(descriptor -> fieldSet.contains(descriptor.name()));
            return this;
        }

        /**
         * Excludes descriptors with matching field names.
         *
         * @param fieldName the field names to exclude
         * @return the updated Operator instance
         */
        @Override
        public Operator exclude(String... fieldName) {
            Set<String> fieldSet = new HashSet<>(Arrays.asList(fieldName));
            this.descriptors = descriptors.filter(descriptor -> !fieldSet.contains(descriptor.name()));
            return this;
        }

        /**
         * Applies modifications to descriptors that match the specified field names.
         *
         * @param fieldNames the field names to match
         * @param modifier   the modification function to apply
         * @return the updated DescriptorModifier instance
         */
        private Operator update(String[] fieldNames, UnaryOperator<Descriptor> modifier) {
            Set<String> fieldsToModify = new HashSet<>(Arrays.asList(fieldNames));
            this.descriptors = descriptors.map(descriptor -> {
                if (fieldsToModify.contains(descriptor.name())) {
                    return modifier.apply(descriptor);
                }
                return descriptor;
            });
            return this;
        }

        /**
         * Marks the specified fields as ignored.
         *
         * @param fieldNames the field names to ignore
         * @return the updated Operator instance
         */
        @Override
        public Operator ignore(String... fieldNames) {
            return update(fieldNames, descriptor -> descriptor.ignore(true));
        }

        /**
         * Marks the specified fields as not ignored.
         *
         * @param fieldNames the field names to not ignore
         * @return the updated Operator instance
         */
        @Override
        public Operator notIgnore(String... fieldNames) {
            return update(fieldNames, descriptor -> descriptor.ignore(false));
        }

        /**
         * Marks the specified fields as optional.
         *
         * @param fieldNames the field names to mark as optional
         * @return the updated Operator instance
         */
        @Override
        public Operator optional(String... fieldNames) {
            return update(fieldNames, descriptor -> descriptor.optional(true));
        }

        /**
         * Marks the specified fields as required (not optional).
         *
         * @param fieldNames the field names to mark as required
         * @return the updated Operator instance
         */
        @Override
        public Operator require(String... fieldNames) {
            return update(fieldNames, descriptor -> descriptor.optional(false));
        }

        @Override
        public List<FieldDescriptor> toField() {
            return this.descriptors.map(descriptor ->
                updateFieldDescriptor(fieldWithPath(descriptor.name())).apply(descriptor)).toList();
        }

        @Override
        public List<SubsectionDescriptor> toSubsection() {
            return this.descriptors.map(descriptor ->
                updateFieldDescriptor(subsectionWithPath(descriptor.name())).apply(descriptor)).toList();
        }

        @Override
        public List<RequestPartDescriptor> toRequestPart() {
            return this.descriptors.map(descriptor -> {
                RequestPartDescriptor newDescriptor = updateIgnorableDescriptor(partWithName(descriptor.name())).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        @Override
        public List<ParameterDescriptor> toParameter() {
            return this.descriptors.map(descriptor -> {
                ParameterDescriptor newDescriptor = updateIgnorableDescriptor(parameterWithName(descriptor.name())).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        @Override
        public List<LinkDescriptor> toLink() {
            return this.descriptors.map(descriptor -> {
                LinkDescriptor newDescriptor = updateIgnorableDescriptor(linkWithRel(descriptor.name())).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        @Override
        public List<HeaderDescriptor> toHeader() {
            return this.descriptors.map(descriptor -> {
                HeaderDescriptor newDescriptor = updateDescriptor(headerWithName(descriptor.name())).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        @Override
        public List<CookieDescriptor> toCookie() {
            return this.descriptors.map(descriptor -> {
                CookieDescriptor newDescriptor = updateIgnorableDescriptor(cookieWithName(descriptor.name())).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        @Override
        public List<Descriptor> toList() {
            return this.descriptors.toList();
        }

        /**
         * Returns a string representation of all descriptor descriptions joined with a space delimiter.
         * Equivalent to toString(" ").
         *
         * @return A string containing all descriptions separated by spaces
         */
        @Override
        public String join() {
            return join(" ");
        }

        /**
         * Returns a string representation of all descriptor descriptions joined with the specified delimiter.
         * Equivalent to toString(delimiter, "", "").
         *
         * @param delimiter The character sequence to use as a delimiter between descriptions
         * @return A string containing all descriptions separated by the specified delimiter
         */
        @Override
        public String join(CharSequence delimiter) {
            return join(delimiter, "", "");
        }

        /**
         * Returns a string representation of all descriptor descriptions with specified delimiter, prefix, and suffix.
         * Extracts descriptions from Descriptors, converts them to strings, and joins them.
         *
         * @param delimiter The character sequence to use as a delimiter between descriptions
         * @param prefix The character sequence to use as a prefix for the resulting string
         * @param suffix The character sequence to use as a suffix for the resulting string
         * @return A string containing all descriptions with the specified formatting
         * @see Descriptor#description() Returns the description field from the Descriptor
         */
        @Override
        public String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
            return this.descriptors
                .map(Descriptor::description)
                .map(Object::toString).collect(Collectors.joining(delimiter, prefix, suffix));
        }

        @Override
        public PathParametersSnippet pathParameters() {
            return RequestDocumentation.pathParameters(toParameter());
        }

        @Override
        public QueryParametersSnippet queryParameters() {
            return RequestDocumentation.queryParameters(toParameter());
        }

        @Override
        public FormParametersSnippet formParameters() {
            return RequestDocumentation.formParameters(toParameter());
        }

        @Override
        public RequestPartsSnippet requestParts() {
            return RequestDocumentation.requestParts(toRequestPart());
        }

        @Override
        public RequestFieldsSnippet requestFields() {
            return PayloadDocumentation.requestFields(toField());
        }

        @Override
        public RequestPartFieldsSnippet requestPartFields(String path) {
            return PayloadDocumentation.requestPartFields(path, toField());
        }

        @Override
        public ResponseFieldsSnippet responseFields() {
            return PayloadDocumentation.responseFields(toField());
        }

        @Override
        public ResponseFieldsSnippet responseFields(String prefix) {
            return PayloadDocumentation.responseFields().andWithPrefix(prefix, toField());
        }

        @Override
        public RequestBodySnippet requestBody() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBodySnippet responseBody() {
            throw new UnsupportedOperationException();
        }

        @Override
        public RequestPartBodySnippet requestPartBody() {
            throw new UnsupportedOperationException();
        }

        @Override
        public LinksSnippet links() {
            return HypermediaDocumentation.links(toLink());
        }

        @Override
        public RequestHeadersSnippet requestHeaders() {
            return HeaderDocumentation.requestHeaders(toHeader());
        }

        @Override
        public ResponseHeadersSnippet responseHeaders() {
            return HeaderDocumentation.responseHeaders(toHeader());
        }

        @Override
        public RequestCookiesSnippet requestCookies() {
            return CookieDocumentation.requestCookies(toCookie());
        }

        @Override
        public ResponseCookiesSnippet responseCookies() {
            return CookieDocumentation.responseCookies(toCookie());
        }
    }
}
