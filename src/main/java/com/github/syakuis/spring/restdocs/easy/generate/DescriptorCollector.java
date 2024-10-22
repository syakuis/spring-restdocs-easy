package com.github.syakuis.spring.restdocs.easy.generate;

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
 * A utility class that provides useful operations for collections of Descriptor objects.
 * <p>
 * This class offers methods to merge and manipulate lists of Descriptor objects efficiently.
 * It ensures that the resulting list contains unique Descriptor objects based on their names,
 * with values from the source list taking precedence over those in the target list.
 * <p>
 * The class is designed to be used as a static utility and cannot be instantiated.
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DescriptorCollector {
    /**
     * Merges two lists of Descriptor objects. If a field exists in both lists,
     * the value from the source list overwrites the one in the target list.
     *
     * @param target The base list to merge into
     * @param source The list to merge from
     * @return A new list containing the merged result
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
     * Updates the given descriptor with the common properties from the source descriptor.
     *
     * @param newDescriptor the descriptor to be updated
     * @param <T>           the type of the descriptor, which must extend AbstractDescriptor
     * @return a function that takes a source descriptor and returns the updated descriptor
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
     * Updates the given ignorable descriptor with the ignorable properties from the source descriptor.
     *
     * @param newDescriptor the ignorable descriptor to be updated
     * @param <T>           the type of the ignorable descriptor, which must extend IgnorableDescriptor
     * @return a function that takes a source descriptor and returns the updated ignorable descriptor
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
     * Updates the given field descriptor with the field-specific properties from the source descriptor.
     *
     * @param newDescriptor the field descriptor to be updated
     * @param <T>           the type of the field descriptor, which must extend FieldDescriptor
     * @return a function that takes a source descriptor and returns the updated field descriptor
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
