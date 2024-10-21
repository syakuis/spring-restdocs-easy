package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.DataClassLoader;
import org.springframework.context.MessageSource;

import java.util.List;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-18
 */
public class ClassLoaderRestDocs extends AbstractRestDocs {
    private final FieldOptionalValidator fieldOptionalValidator;

    public ClassLoaderRestDocs(MessageSource messageSource, List<Class<?>> validationGroups) {
        super(messageSource);
        this.fieldOptionalValidator = new FieldOptionalValidator(validationGroups);
    }

    // todo generate
    public List<Descriptor> toList(Class<?> objectType) {
//        ConstraintDescriptions userConstraints = super.getConstraintDescriptions(objectType);

        return DataClassLoader.of(objectType).toList().stream().map(it -> {

//                boolean isValid = fieldOptionalValidator.hasValidationConstraint(it.field());

                return Descriptor.builder()
                    .name(it.fieldName())
                    .type(JsonFieldTypeMapper.get(it.fieldType()))
                    .description(super.getMessage(it))
                    .optional(fieldOptionalValidator.isFieldOptional(it.field()))
                    .ignore(false)
                    /*.attributes(
                        isValid ?
                            List.of(key("constraints")
                                .value(String.join("\n\n", userConstraints.descriptionsForProperty(it.fieldName()))))
                            : Collections.emptyList()
                    )*/
                    .build();
            }
        ).toList();
    }
}
