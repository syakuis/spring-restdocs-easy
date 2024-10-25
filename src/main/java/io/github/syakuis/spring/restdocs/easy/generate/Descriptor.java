package io.github.syakuis.spring.restdocs.easy.generate;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a field descriptor for "Spring REST Docs Easy".
 * This class extends Spring REST Docs' field documentation capabilities by providing
 * additional features for more flexible and detailed API documentation.
 *
 * <p>Features:</p>
 * - Supports field path prefixing for nested structures (e.g., "user.", "[].user.")
 * - Uses Spring REST Docs JsonFieldType for type documentation
 * - Optional/required field marking
 * - Custom attribute support for constraints
 * - Selective field documentation with ignore option
 *
 * @author Seok Kyun. Choi.
 * @since 2023-07-15
 */
@Getter
@Accessors(fluent = true)
@Builder
public class Descriptor {
    /**
     * Optional prefix for the field path, useful for nested objects
     * Example: "user." for "user.name"
     * Example: "[].user." for "[].user.name"
     */
    private final String prefix;

    /**
     * The name of the field being documented
     */
    private final String name;

    /**
     * The Spring REST Docs JsonFieldType that specifies how this field should be documented
     * (e.g., STRING, NUMBER, BOOLEAN, ARRAY, OBJECT)
     */
    @Builder.Default
    private JsonFieldType type = JsonFieldType.STRING;

    /**
     * The description of the field that will appear in the documentation
     * Can be a string or a message source key
     */
    private Object description;

    /**
     * Indicates whether the field is optional in the API
     * true = optional, false = required
     */
    @Builder.Default
    private boolean optional = false;

    /**
     * Controls whether this field should be excluded from documentation
     * true = exclude from docs, false = include in docs
     */
    @Builder.Default
    private boolean ignore = false;

    /**
     * Additional attributes for the field documentation
     * Typically used for constraints or other metadata
     */
    private Attributes.Attribute[] attributes;

    /**
     * Sets the JSON field type for this descriptor.
     *
     * @param type the Spring REST Docs JsonFieldType to use
     * @return this descriptor instance for method chaining
     */
    public Descriptor type(JsonFieldType type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the description for this field.
     * The description can be a direct string or a message source key in the format {key}.
     *
     * @param description the field description or message key
     * @return this descriptor instance for method chaining
     */
    public Descriptor description(Object description) {
        this.description = description;
        return this;
    }

    /**
     * Sets whether this field is optional in the API documentation.
     *
     * @param optional true if the field is optional, false if required
     * @return this descriptor instance for method chaining
     */
    public Descriptor optional(boolean optional) {
        this.optional = optional;
        return this;
    }

    /**
     * Sets whether this field should be ignored in the documentation.
     *
     * @param ignore true to exclude from documentation, false to include
     * @return this descriptor instance for method chaining
     */
    public Descriptor ignore(boolean ignore) {
        this.ignore = ignore;
        return this;
    }

    /**
     * Sets additional attributes for this field's documentation.
     * Typically used for adding validation constraints or other metadata.
     *
     * @param attributes array of additional attributes
     * @return this descriptor instance for method chaining
     */
    public Descriptor attributes(Attributes.Attribute[] attributes) {
        this.attributes = attributes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Descriptor that = (Descriptor) o;
        return optional == that.optional && ignore == that.ignore && Objects.equals(name, that.name) && type == that.type && Objects.equals(description, that.description) && Arrays.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Boolean.hashCode(optional);
        result = 31 * result + Boolean.hashCode(ignore);
        result = 31 * result + Arrays.hashCode(attributes);
        return result;
    }

    @Override
    public String toString() {
        return "Descriptor{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", description=" + description +
            ", optional=" + optional +
            ", ignore=" + ignore +
            ", attributes=" + Arrays.toString(attributes) +
            '}';
    }
}