package io.github.syakuis.spring.restdocs.easy.generate;

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

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.syakuis.spring.restdocs.easy.generate.DescriptorCollector.*;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;

/**
 * Default implementation of the RestDocs interface for "Spring REST Docs Easy".
 * Provides core functionality for generating API documentation descriptors with
 * support for message internationalization and custom type mapping.
 *
 * <p>Features:</p>
 * - Generates documentation descriptors from Java classes
 * - Supports validation groups for conditional documentation
 * - Handles nested object documentation with prefixes
 * - Provides flexible descriptor modification capabilities
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
class DefaultRestDocs implements RestDocs {
    private final MessageSource messageSource;
    private final ClassDescriptorGenerator classDescriptorGenerator;

    /**
     * Creates a new instance with specified message source and type mapper.
     *
     * @param messageSource source for resolving i18n messages
     * @param jsonFieldTypeMapper custom type mapping configuration
     */
    public DefaultRestDocs(MessageSource messageSource, JsonFieldTypeMapper jsonFieldTypeMapper) {
        this.messageSource = messageSource;
        this.classDescriptorGenerator = new ClassDescriptorGenerator(messageSource, jsonFieldTypeMapper);
    }

    /**
     * {@inheritDoc}
     * Creates a new HeadersGenerator with the configured message source.
     */
    @Override
    public HeadersGenerator headers() {
        return new DefaultHeadersGenerator(messageSource);
    }

    /**
     * {@inheritDoc}
     * Creates a new ParamsGenerator with the configured message source.
     */
    @Override
    public ParamsGenerator params() {
        return new DefaultParamsGenerator(messageSource);
    }

    /**
     * {@inheritDoc}
     * Creates a new DescriptorsGenerator with the configured message source.
     */
    @Override
    public DescriptorsGenerator descriptors() {
        return new DefaultDescriptorsGenerator(messageSource);
    }

    public Operator generate(Class<?> targetClass) {
        return generate(targetClass, new Class<?>[0]);
    }

    /**
     * Generates an Operator for the specified target class.
     *
     * @param targetClass the class to generate descriptors for
     * @return a new Operator instance
     */
    @Override
    public Operator generate(Class<?> targetClass, Class<?>... validGroups) {
        return generate(null, targetClass, validGroups);
    }

    public Operator generate(String prefix, Class<?> targetClass) {
        return generate(prefix, targetClass, new Class<?>[0]);
    }

    @Override
    public Operator generate(String prefix, Class<?> targetClass, Class<?>... validGroups) {
        return new DefaultOperator(classDescriptorGenerator.generate(prefix, targetClass, validGroups), classDescriptorGenerator);
    }

    /**
     * Default implementation of the Operator interface for "Spring REST Docs Easy".
     * Provides methods for manipulating and transforming documentation descriptors.
     */
    static class DefaultOperator implements Operator {
        private Stream<Descriptor> descriptors;
        private ClassDescriptorGenerator classDescriptorGenerator;

        /**
         * Creates a new operator with the specified descriptors.
         *
         * @param descriptors initial list of descriptors
         */
        public DefaultOperator(List<Descriptor> descriptors) {
            this.descriptors = descriptors.stream();
        }

        /**
         * Creates a new operator with descriptors and generator.
         *
         * @param descriptors initial list of descriptors
         * @param classDescriptorGenerator generator for additional descriptors
         */
        public DefaultOperator(List<Descriptor> descriptors, ClassDescriptorGenerator classDescriptorGenerator) {
            this.descriptors = descriptors.stream();
            this.classDescriptorGenerator = classDescriptorGenerator;
        }

        /**
         * {@inheritDoc}
         * Adds descriptors generated from the specified class with prefix support.
         *
         * @throws IllegalStateException if classDescriptorGenerator is not set
         */
        @Override
        public Operator addAll(String prefix, Class<?> targetClass, Class<?>... validGroups) {
            if (classDescriptorGenerator == null) {
                throw new IllegalStateException("classDescriptorGenerator not set");
            }

            var data = new LinkedList<>(descriptors.toList());
            data.addAll(classDescriptorGenerator.generate(prefix, targetClass, validGroups));
            this.descriptors = data.stream();
            return this;
        }

        /**
         * Adds additional descriptors to the existing stream.
         *
         * @param descriptor the descriptors to add
         * @return the updated Operator instance
         */
        @Override
        public Operator addAll(Descriptor... descriptor) {
            addAll(Arrays.asList(descriptor));
            return this;
        }

        @Override
        public Operator addAll(List<Descriptor> descriptor) {
            this.descriptors = DescriptorCollector.merge(descriptors.toList(), descriptor).stream();
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

        /**
         * Utility method to add prefix to field names.
         * Handles null or blank prefix cases appropriately.
         *
         * @param prefix prefix to add to the name
         * @param name field name
         * @return prefixed name or original name if prefix is null/blank
         */
        private String prefix(String prefix, String name) {
            return prefix != null && !prefix.isBlank() ? prefix + name : name;
        }

        /**
         * Converts descriptors to Spring REST Docs FieldDescriptor objects.
         * Applies prefix if specified and updates field properties.
         *
         * @return list of configured FieldDescriptor objects
         * @see org.springframework.restdocs.payload.PayloadDocumentation#fieldWithPath
         */
        @Override
        public List<FieldDescriptor> toField() {
            return this.descriptors.map(descriptor ->
                updateFieldDescriptor(fieldWithPath(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor)).toList();
        }

        /**
         * Converts descriptors to Spring REST Docs SubsectionDescriptor objects.
         * Used for documenting nested object structures.
         *
         * @return list of configured SubsectionDescriptor objects
         * @see org.springframework.restdocs.payload.PayloadDocumentation#subsectionWithPath
         */
        @Override
        public List<SubsectionDescriptor> toSubsection() {
            return this.descriptors.map(descriptor ->
                updateFieldDescriptor(subsectionWithPath(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor)).toList();
        }

        /**
         * Converts descriptors to Spring REST Docs RequestPartDescriptor objects.
         * Handles optional status and prefix for multipart request documentation.
         *
         * @return list of configured RequestPartDescriptor objects
         * @see org.springframework.restdocs.request.RequestDocumentation#partWithName
         */
        @Override
        public List<RequestPartDescriptor> toRequestPart() {
            return this.descriptors.map(descriptor -> {
                RequestPartDescriptor newDescriptor = updateIgnorableDescriptor(partWithName(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        /**
         * Converts descriptors to Spring REST Docs ParameterDescriptor objects.
         * Used for documenting URL parameters with optional status support.
         *
         * @return list of configured ParameterDescriptor objects
         * @see org.springframework.restdocs.request.RequestDocumentation#parameterWithName
         */
        @Override
        public List<ParameterDescriptor> toParameter() {
            return this.descriptors.map(descriptor -> {
                ParameterDescriptor newDescriptor = updateIgnorableDescriptor(parameterWithName(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        /**
         * Converts descriptors to Spring REST Docs LinkDescriptor objects.
         * Used for documenting hypermedia links with optional status support.
         *
         * @return list of configured LinkDescriptor objects
         * @see org.springframework.restdocs.hypermedia.HypermediaDocumentation#linkWithRel
         */
        @Override
        public List<LinkDescriptor> toLink() {
            return this.descriptors.map(descriptor -> {
                LinkDescriptor newDescriptor = updateIgnorableDescriptor(linkWithRel(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        /**
         * Converts descriptors to Spring REST Docs HeaderDescriptor objects.
         * Used for documenting HTTP headers with optional status support.
         *
         * @return list of configured HeaderDescriptor objects
         * @see org.springframework.restdocs.headers.HeaderDocumentation#headerWithName
         */
        @Override
        public List<HeaderDescriptor> toHeader() {
            return this.descriptors.map(descriptor -> {
                HeaderDescriptor newDescriptor = updateDescriptor(headerWithName(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        /**
         * Converts descriptors to Spring REST Docs CookieDescriptor objects.
         * Used for documenting cookies with optional status support.
         *
         * @return list of configured CookieDescriptor objects
         * @see org.springframework.restdocs.cookies.CookieDocumentation#cookieWithName
         */
        @Override
        public List<CookieDescriptor> toCookie() {
            return this.descriptors.map(descriptor -> {
                CookieDescriptor newDescriptor = updateIgnorableDescriptor(cookieWithName(
                    prefix(descriptor.prefix(), descriptor.name())
                )).apply(descriptor);

                if (descriptor.optional()) {
                    newDescriptor.optional();
                }

                return newDescriptor;
            }).toList();
        }

        /**
         * Returns the raw list of descriptors without any conversion.
         * Provides access to the original descriptor objects.
         *
         * @return list of original Descriptor objects
         */
        @Override
        public List<Descriptor> toList() {
            return this.descriptors.toList();
        }

        /**
         * Returns a string representation of all descriptor descriptions joined with a space delimiter.
         * Equivalent to {@code join(" ")}.
         *
         * @return A string containing all descriptions separated by spaces
         */
        @Override
        public String join() {
            return join(" ");
        }

        /**
         * Returns a string representation of all descriptor descriptions joined with the specified delimiter.
         * Equivalent to {@code join(delimiter, "", "")}.
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

        /**
         * Operation not supported in current version.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public RequestBodySnippet requestBody() {
            throw new UnsupportedOperationException();
        }

        /**
         * Operation not supported in current version.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public ResponseBodySnippet responseBody() {
            throw new UnsupportedOperationException();
        }

        /**
         * Operation not supported in current version.
         *
         * @throws UnsupportedOperationException always
         */
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
