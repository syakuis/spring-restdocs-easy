package com.github.syakuis.spring.restdocs.easy.generate;

import com.github.syakuis.spring.restdocs.easy.core.DataClassMetadata;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * This class handles the retrieval of field descriptions and enum constant messages from a MessageSource.
 * It is used to generate documentation for fields, including handling special cases for enums.
 * If the field is of an enum type, it retrieves individual messages for each enum constant.
 * For non-enum fields, it simply fetches the message based on the field's name.
 * The messages are resolved using the Spring MessageSource, which allows for internationalization (i18n)
 * and customization based on the application's message properties files.
 *
 * @author Seok Kyun Choi
 * @since 2024-01-12
 */
public class DescriptionMessageSource {
    private final MessageSource messageSource;

    /**
     * Constructs a new DescriptionMessageSource with the provided MessageSource.
     * The MessageSource is used to resolve messages for both fields and enums.
     *
     * @param messageSource The MessageSource for resolving messages.
     */
    public DescriptionMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Retrieves the message for a field described by DataClassMetadata.
     * If the field is an enum, it fetches messages for each enum constant as well.
     *
     * @param dataClassMetadata Metadata representing the field.
     * @return The resolved message or formatted enum constant messages.
     */
    public String getMessage(DataClassMetadata dataClassMetadata) {
        return isEnumField(dataClassMetadata)
            ? getEnumMessages(dataClassMetadata)
            : getMessageForCode(dataClassMetadata.name() + "." + dataClassMetadata.fieldName(), dataClassMetadata.fieldName());
    }

    /**
     * Checks if the field type in DataClassMetadata is an enum.
     *
     * @param dataClassMetadata Metadata representing the field.
     * @return true if the field is an enum, false otherwise.
     */
    private boolean isEnumField(DataClassMetadata dataClassMetadata) {
        return Enum.class.isAssignableFrom(dataClassMetadata.fieldType());
    }

    /**
     * Retrieves messages for an enum field, including a message for each enum constant.
     *
     * @param dataClassMetadata Metadata representing the enum field.
     * @return The formatted messages for the enum and its constants.
     */
    private String getEnumMessages(DataClassMetadata dataClassMetadata) {
        String baseMessage = getMessageForCode(dataClassMetadata.name() + "." + dataClassMetadata.fieldName(), dataClassMetadata.fieldName());

        List<String> enumMessages = new ArrayList<>();
        for (Object enumConstant : dataClassMetadata.fieldType().getEnumConstants()) {
            String constantName = ((Enum<?>) enumConstant).name();
            String constantMessage = getMessageForCode(dataClassMetadata.fieldType().getName() + "." + constantName, null);
            enumMessages.add(constantMessage != null ? constantName + " : " + constantMessage : constantName);
        }

        return formatEnumFieldOutput(baseMessage, enumMessages);
    }

    /**
     * Formats the output for an enum field, combining its base message and its constants' messages.
     *
     * @param baseMessage The base message for the field.
     * @param enumMessages A list of messages for each enum constant.
     * @return A formatted string containing the field's message and the constants' messages.
     */
    private String formatEnumFieldOutput(String baseMessage, List<String> enumMessages) {
        if (baseMessage == null || enumMessages.isEmpty()) {
            throw new IllegalArgumentException("Base message and enum messages must not be null or empty.");
        }

        return baseMessage + System.lineSeparator() + System.lineSeparator() +
            String.join(" + " + System.lineSeparator(), enumMessages);
    }

    /**
     * Retrieves a message from the MessageSource using a code, or returns a default message if no message is found.
     *
     * @param code The message code to look up in the MessageSource.
     * @param defaultMessage The default message to return if the code is not found.
     * @return The resolved message or the default message.
     */
    private String getMessageForCode(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
    }

    /**
     * Resolves a message based on a given expression, which may be in the form `{messageCode}`.
     * If the expression is in this format, the corresponding message is retrieved from the MessageSource.
     *
     * @param expression The expression which may contain a message code in curly braces.
     * @return The resolved message or the expression itself if not formatted as `{messageCode}`.
     */
    public String getMessageByExpression(Object expression) {
        return getMessageByExpression(expression, String.valueOf(expression));
    }

    /**
     * Resolves a message from an expression, with an optional default message.
     * If the expression contains a message code in curly braces (e.g. `{messageCode}`),
     * the message is retrieved from the MessageSource.
     *
     * @param expression The expression that may contain a message code.
     * @param defaultMessage The default message to return if no message is found.
     * @return The resolved message or the default message.
     */
    public String getMessageByExpression(Object expression, String defaultMessage) {
        if (expression == null) {
            return defaultMessage;
        }

        String newExpression = String.valueOf(expression);
        if (newExpression.startsWith("{") && newExpression.endsWith("}")) {
            String code = newExpression.substring(1, newExpression.length() - 1);
            return getMessageForCode(code, defaultMessage);
        }

        return Objects.toString(expression, defaultMessage);
    }
}
