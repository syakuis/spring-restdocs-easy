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

    public ClassLoaderRestDocs(MessageSource messageSource, List<MessageFormatter> messageFormatters, List<Class<?>> validationGroups) {
        super(messageSource, messageFormatters);
        this.fieldOptionalValidator = new FieldOptionalValidator(validationGroups);
    }

    private List<Description> of(Class<?> objectType) {
//        ConstraintDescriptions userConstraints = super.getConstraintDescriptions(objectType);

        return DataClassLoader.of(objectType).toList().stream().map(it -> {

                boolean isValid = fieldOptionalValidator.hasValidationConstraint(it.field());

                return Description.builder()
                    .name(it.fieldName())
                    .type(JsonFieldTypeMapper.get(it.fieldType()))
                    // todo messageSource
                    .desc(it.field())
                    // todo optional 과 ignore 역할 확인
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
