package io.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * Handles the extraction and formatting of field validation constraints for "Spring REST Docs Easy".
 * Converts Jakarta validation annotations into human-readable descriptions for API documentation.
 *
 * <p>Supported constraints include:</p>
 * - Size constraints ({@code @Size(min = 1, max = 100)})
 * - Pattern matching ({@code @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,6}$")})
 * - Range validations ({@code @Min(0)}, {@code @Max(100)})
 * - Required field validations ({@code @NotNull}, {@code @NotEmpty}, {@code @NotBlank})
 * - Custom constraints with valid documentation
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Given a DTO class with validation annotations
 * public class UserDto {
 *     @NotNull
 *     @Size(min = 3, max = 50)
 *     private String name;
 *
 *     @Email
 *     @NotEmpty
 *     private String email;
 * }
 *
 * // Generate constraint descriptions
 * ClassFieldConstraintDescriptions descriptions =
 *     new ClassFieldConstraintDescriptions(UserDto.class);
 *
 * // Get formatted constraints for a field
 * Attributes.Attribute[] nameConstraints = descriptions.getConstraints("name");
 * // Results in: "Must not be null\n\nMust be between 3 and 50 characters"
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 * @see org.springframework.restdocs.constraints.ConstraintDescriptions
 * @see jakarta.validation.constraints
 */
class ClassFieldConstraintDescriptions {
    private final ConstraintDescriptions constraintDescriptions;

    /**
     * Creates a new constraint description handler for "Spring REST Docs Easy".
     *
     * @param targetClass the class whose fields' constraints will be described
     */
    ClassFieldConstraintDescriptions(Class<?> targetClass) {
        this.constraintDescriptions = new ConstraintDescriptions(targetClass);
    }

    /**
     * Retrieves formatted constraint descriptions for a specific field.
     * Combines all constraints into a single attribute with descriptions separated by newlines.
     * The resulting descriptions are internationalized if message keys are provided in the constraints.
     *
     * @param fieldName the name of the field whose constraints should be described
     * @return an array containing a single attribute with all constraint descriptions
     */
    public Attributes.Attribute[] getConstraints(String fieldName) {
        return new Attributes.Attribute[] {
            key("constraints").value(String.join("\n\n", constraintDescriptions.descriptionsForProperty(fieldName)))
        };
    }
}
