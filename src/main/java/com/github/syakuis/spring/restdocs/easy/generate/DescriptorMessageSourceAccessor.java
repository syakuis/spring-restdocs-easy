package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.DataClassMetadata;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Retrieves messages for fields, including enum constants, from a MessageSource.
 * This class helps in generating documentation for fields and enums.
 * @author Seok Kyun. Choi.
 * @since 2024-01-12
 */
// todo rename AbstractDescriptorMessageSource
public class DescriptorMessageSourceAccessor {
    private final MessageSource messageSource;

    public DescriptorMessageSourceAccessor(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Retrieves the message for the given data class metadata.
     * If the field type is an enum, it retrieves enum-specific messages.
     *
     * @param dataClassMetadata Metadata of the data class field.
     * @return The message associated with the field or enum constants.
     */
    public String getMessage(DataClassMetadata dataClassMetadata) {
        return isEnumField(dataClassMetadata)
            ? getEnumMessages(dataClassMetadata)
            : getFieldMessage(dataClassMetadata);
    }

    /**
     * Determines if the field is of an enum type.
     *
     * @param dataClassMetadata Metadata of the data class field.
     * @return True if the field is an enum, false otherwise.
     */
    private boolean isEnumField(DataClassMetadata dataClassMetadata) {
        return Enum.class.isAssignableFrom(dataClassMetadata.fieldType());
    }

    /**
     * Retrieves the message for a non-enum field.
     *
     * @param dataClassMetadata Metadata of the data class field.
     * @return The message associated with the field.
     */
    private String getFieldMessage(DataClassMetadata dataClassMetadata) {
        String code = dataClassMetadata.name() + "." + dataClassMetadata.fieldName();
        return resolveMessageOrDefault(code, dataClassMetadata.fieldName());
    }

    /**
     * Retrieves the messages for an enum field, including descriptions for each constant.
     *
     * @param dataClassMetadata Metadata of the data class field.
     * @return The concatenated messages for the enum and its constants.
     */
    private String getEnumMessages(DataClassMetadata dataClassMetadata) {
        String baseMessage = getFieldMessage(dataClassMetadata);

        Object[] enumConstants = dataClassMetadata.fieldType().getEnumConstants();
        List<String> enumMessages = new ArrayList<>();
        for (Object enumConstant : enumConstants) {
            String name = ((Enum<?>) enumConstant).name();
            String enumMessage = resolveMessageOrDefault(dataClassMetadata.fieldType().getName() + "." + name, null);
            enumMessages.add(formatEnumConstantMessage(name, enumMessage));
        }

        return formatEnumFieldOutput(baseMessage, enumMessages);
    }

    /**
     * Formats a description string using the given enum constant name and its associated message.
     *
     * @param name The name of the enum constant.
     * @param message The message associated with the enum constant.
     * @return The formatted description string.
     */
    private String formatEnumConstantMessage(String name, String message) {
        return message != null ? String.format("%s : %s", name, message) : name;
    }

    /**
     * Formats the output for an enum field and its constants.
     *
     * @param baseMessage The base message for the field.
     * @param enumMessages The list of formatted messages for each enum constant.
     * @return The complete formatted output string.
     */
    private String formatEnumFieldOutput(String baseMessage, List<String> enumMessages) {
        if (baseMessage == null || enumMessages == null || enumMessages.isEmpty()) {
            throw new IllegalArgumentException("Base message and enum messages must not be null or empty.");
        }

        StringBuilder output = new StringBuilder();
        String lineSeparator = System.lineSeparator();

        output.append(baseMessage)
            .append(lineSeparator)
            .append(lineSeparator);

        output.append(String.join(" + " + lineSeparator, enumMessages));

        return output.toString();
    }

    /**
     * Resolves a message from the MessageSource using a code and default message.
     *
     * @param code The code to look up in the MessageSource.
     * @param defaultMessage The default message if no code is found.
     * @return The resolved message or the default message if not found.
     */
    // todo rename getMessageOrDefault
    private String resolveMessageOrDefault(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
    }

    public String getMessageByExpression(Object expression) {
        return getMessageByExpression(expression, String.valueOf(expression));
    }

    public String getMessageByExpression(Object expression, String defaultMessage) {
        if (expression == null) {
            return defaultMessage;
        }

        String newExpression = String.valueOf(expression);
        if (newExpression.startsWith("{") && newExpression.endsWith("}")) {
            String code = newExpression.substring(1, newExpression.length() - 1);
            return resolveMessageOrDefault(code, defaultMessage);
        }

        return Objects.toString(expression, defaultMessage);
    }
}
