package io.github.syakuis.spring.restdocs.easy.generate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
class FieldOptionalValidatorTest {

    private FieldOptionalValidator validatorNoGroups;
    private FieldOptionalValidator validatorWithGroups;

    @BeforeEach
    void setUp() {
        // Set up validator with no validation groups
        validatorNoGroups = new FieldOptionalValidator(Collections.emptyList());

        // Set up validator with validation groups
        validatorWithGroups = new FieldOptionalValidator(List.of(DefaultGroup.class));
    }

    // Sample class for testing fields with annotations
    private static class SampleClass {
        @NotNull
        private String notNullField;

        @NotEmpty
        private String notEmptyField;

        @NotBlank
        private String notBlankField;

        private String optionalField;
    }

    private interface DefaultGroup {}
    private interface AnotherGroup {}

    // --- Test cases ---

    @Test
    void testFieldIsMandatoryWithoutGroups() throws NoSuchFieldException {
        Field field = SampleClass.class.getDeclaredField("notNullField");
        assertFalse(validatorNoGroups.isFieldOptional(field));

        field = SampleClass.class.getDeclaredField("notEmptyField");
        assertFalse(validatorNoGroups.isFieldOptional(field));

        field = SampleClass.class.getDeclaredField("notBlankField");
        assertFalse(validatorNoGroups.isFieldOptional(field));
    }

    @Test
    void testFieldIsOptionalWithoutGroups() throws NoSuchFieldException {
        Field field = SampleClass.class.getDeclaredField("optionalField");
        assertTrue(validatorNoGroups.isFieldOptional(field));
    }

    @Test
    void testFieldIsMandatoryWithGroups() throws NoSuchFieldException {
        // Test with DefaultGroup group assigned to validation
        Field field = SampleClass.class.getDeclaredField("notNullField");

        // Even with groups defined, field should still be mandatory as no groups are defined in @NotNull
        assertFalse(validatorWithGroups.isFieldOptional(field));

        // Test a field without any validation annotations
        field = SampleClass.class.getDeclaredField("optionalField");
        assertTrue(validatorWithGroups.isFieldOptional(field));
    }

    @Test
    void testFieldWithGroupsShouldBeOptionalIfGroupDoesNotMatch() throws NoSuchFieldException {
        // Simulate annotation with groups that don't match
        Field field = SampleClassWithGroup.class.getDeclaredField("notNullWithGroup");

        // Group should not match, so field should be considered optional
        assertTrue(validatorWithGroups.isFieldOptional(field));
    }

    @Test
    void testFieldWithMatchingGroupIsMandatory() throws NoSuchFieldException {
        // Simulate annotation with matching group
        Field field = SampleClassWithGroup.class.getDeclaredField("notNullWithDefaultGroup");

        // Field should be mandatory because the validation group matches
        assertFalse(validatorWithGroups.isFieldOptional(field));
    }

    // Additional class with group annotations for testing
    private static class SampleClassWithGroup {
        @NotNull(groups = AnotherGroup.class)
        private String notNullWithGroup;

        @NotNull(groups = DefaultGroup.class)
        private String notNullWithDefaultGroup;
    }

    // Test class with validation constraints
    private static class SampleClass2 {
        @NotNull
        private String notNullField;

        private String optionalFieldWithoutConstraint;
    }

    @Test
    void testHasValidationConstraint() throws NoSuchFieldException {
        FieldOptionalValidator validator = new FieldOptionalValidator(List.of());

        Field field = SampleClass2.class.getDeclaredField("notNullField");
        assertTrue(validator.hasValidationConstraint(field));

        field = SampleClass2.class.getDeclaredField("optionalFieldWithoutConstraint");
        assertFalse(validator.hasValidationConstraint(field));
    }
}
