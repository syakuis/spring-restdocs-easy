package com.github.syakuis.spring.restdocs.easy.generate;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-15
 */
@Getter
@Accessors(fluent = true)
@Builder
public class Descriptor {
    // Represents the name of the field.
    private final String name;

    // Indicates the data type of the field, with a default value of JsonFieldType.STRING.
    @Builder.Default
    private JsonFieldType type = JsonFieldType.STRING;

    private Object description;

    // Documents that the field is optional and not mandatory.
    @Builder.Default
    private boolean optional = false;

    // Excludes the field from documentation.
    @Builder.Default
    private boolean ignore = false;

    // Contains additional attributes for the field.
    private Attributes.Attribute[] attributes;

    public Descriptor type(JsonFieldType type) {
        this.type = type;
        return this;
    }

    public Descriptor description(Object description) {
        this.description = description;
        return this;
    }

    public Descriptor optional(boolean optional) {
        this.optional = optional;
        return this;
    }

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