package io.github.syakuis.spring.restdocs.easy.generate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.AbstractDescriptor;
import org.springframework.restdocs.snippet.IgnorableDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A utility class for managing and manipulating collections of descriptors in Spring REST Docs Easy.
 * Provides functionality to merge and transform descriptor collections while maintaining
 * documentation properties and relationships with Spring REST Docs.
 *
 * <p>Key features:</p>
 * - Merges descriptor lists with source precedence
 * - Updates descriptor properties (description, type, optional status)
 * - Handles different Spring REST Docs descriptor types
 * - Preserves documentation attributes and constraints
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Merge descriptors with overrides
 * List<Descriptor> merged = DescriptorCollector.merge(baseDescriptors, overrideDescriptors);
 *
 * // Transform to Spring REST Docs FieldDescriptor
 * FieldDescriptor fieldDesc = DescriptorCollector
 *     .updateFieldDescriptor(new FieldDescriptor("field"))
 *     .apply(descriptor);
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DescriptorCollector {

    /**
     * Merges two lists of Descriptor objects, with source descriptors taking precedence.
     * When duplicate field names are found, the source descriptor's properties override
     * the target descriptor's properties.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Target list
     * List<Descriptor> target = List.of(
     *     new Descriptor("name", "Name field"),
     *     new Descriptor("age", "Age field")
     * );
     *
     * // Source list (overrides)
     * List<Descriptor> source = List.of(
     *     new Descriptor("name", "Updated name field")
     * );
     *
     * // Result: "name" from source, "age" from target
     * List<Descriptor> merged = DescriptorCollector.merge(target, source);
     * }</pre>
     *
     * @param target the base list of descriptors to merge into
     * @param source the list of descriptors that will override matching fields in target
     * @return a new list containing all unique descriptors with source taking precedence
     */
    public static List<Descriptor> merge(List<Descriptor> target, List<Descriptor> source) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>(target);
        }

        Map<String, Descriptor> map = target.stream()
            .collect(Collectors.toMap(Descriptor::name, descriptor -> descriptor, (existing, replacement) -> replacement));

        source.forEach(descriptor -> map.put(descriptor.name(), descriptor));

        return new ArrayList<>(map.values());
    }

    /**
     * Updates a Spring REST Docs AbstractDescriptor with common properties.
     * Copies description and attributes from the source descriptor if present.
     *
     * <p>Example:</p>
     * <pre>{@code
     * AbstractDescriptor<?> updated = updateDescriptor(newDescriptor)
     *     .apply(sourceDescriptor);  // Copies description and attributes
     * }</pre>
     *
     * @param newDescriptor the descriptor to be updated
     * @param <T> the type of the descriptor, which must extend AbstractDescriptor
     * @return a function that applies the updates to the descriptor
     */
    public static <T extends AbstractDescriptor<T>> Function<Descriptor, T> updateDescriptor(T newDescriptor) {
        return descriptor -> {
            if (descriptor.description() != null) {
                newDescriptor.description(descriptor.description());
            }

            if (descriptor.attributes() != null && descriptor.attributes().length > 0) {
                newDescriptor.attributes(descriptor.attributes());
            }

            return newDescriptor;
        };
    }

    /**
     * Updates a Spring REST Docs IgnorableDescriptor with common properties and ignore status.
     * Extends updateDescriptor by adding ignore status handling.
     *
     * <p>Example:</p>
     * <pre>{@code
     * IgnorableDescriptor<?> updated = updateIgnorableDescriptor(newDescriptor)
     *     .apply(sourceDescriptor);  // Copies properties and ignore status
     * }</pre>
     *
     * @param newDescriptor the ignorable descriptor to be updated
     * @param <T> the type of the descriptor, which must extend IgnorableDescriptor
     * @return a function that applies the updates to the descriptor
     */
    public static <T extends IgnorableDescriptor<T>> Function<Descriptor, T> updateIgnorableDescriptor(T newDescriptor) {
        return descriptor -> {
            updateDescriptor(newDescriptor).apply(descriptor);

            if (descriptor.ignore()) {
                newDescriptor.ignored();
            }

            return newDescriptor;
        };
    }

    /**
     * Updates a Spring REST Docs FieldDescriptor with all properties including field-specific ones.
     * Extends updateIgnorableDescriptor by adding optional status and field type handling.
     *
     * <p>Example:</p>
     * <pre>{@code
     * FieldDescriptor updated = updateFieldDescriptor(newDescriptor)
     *     .apply(sourceDescriptor);  // Copies all properties including optional status and type
     * }</pre>
     *
     * @param newDescriptor the field descriptor to be updated
     * @param <T> the type of the descriptor, which must extend FieldDescriptor
     * @return a function that applies the updates to the descriptor
     */
    public static <T extends FieldDescriptor> Function<Descriptor, T> updateFieldDescriptor(T newDescriptor) {
        return descriptor -> {
            updateIgnorableDescriptor(newDescriptor).apply(descriptor);

            if (descriptor.optional()) {
                newDescriptor.optional();
            }

            if (descriptor.type() != null) {
                newDescriptor.type(descriptor.type());
            }

            return newDescriptor;
        };
    }
}
