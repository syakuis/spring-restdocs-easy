package io.github.syakuis.spring.restdocs.easy.generate;

import io.github.syakuis.spring.restdocs.easy.core.ClassFieldMetadata;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Core message resolution component for "Spring REST Docs Easy" that handles
 * field descriptions and enum constant documentation.
 *
 * <p>Key features:</p>
 * - Resolves field descriptions using Spring's MessageSource
 * - Special handling for enum fields and their constants
 * - Supports message expression resolution (e.g., {messageCode})
 * - Internationalization (i18n) support through MessageSource
 *
 * <p>Message resolution patterns:</p>
 * - Regular fields: "{package}.{class}.{field}"
 * - Enum fields: "{package}.{enum}" for type, "{package}.{enum}.{constant}" for each constant
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Message properties file:
 * com.example.UserDto.email=User email address
 * com.example.UserStatus=User account status
 * com.example.UserStatus.ACTIVE=Active user account
 * com.example.UserStatus.INACTIVE=Deactivated user account
 *
 * // Usage in code:
 * DescriptionMessageSource source = new DescriptionMessageSource(messageSource);
 *
 * // Regular field description
 * String emailDesc = source.getMessage(emailFieldMetadata);
 * // Result: "User email address"
 *
 * // Enum field description
 * String statusDesc = source.getMessage(statusFieldMetadata);
 * // Result:
 * // User account status
 * //
 * // ACTIVE : Active user account +
 * // INACTIVE : Deactivated user account
 * }</pre>
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
     * Retrieves the message for a field using its metadata.
     * For regular fields, looks up a single message.
     * For enum fields, combines the enum type message with formatted constant messages.
     *
     * @param classFieldMetadata metadata containing field information
     * @return resolved message(s) for the field
     */
    public String getMessage(ClassFieldMetadata classFieldMetadata) {
        return isEnumField(classFieldMetadata)
            ? getEnumMessages(classFieldMetadata)
            : getMessageForCode(classFieldMetadata.packageClassName() + "." + classFieldMetadata.name(), classFieldMetadata.name());
    }

    /**
     * Checks if the field type in ClassFieldMetadata is an enum.
     *
     * @param classFieldMetadata Metadata representing the field.
     * @return true if the field is an enum, false otherwise.
     */
    private boolean isEnumField(ClassFieldMetadata classFieldMetadata) {
        return Enum.class.isAssignableFrom(classFieldMetadata.type());
    }

    /**
     * Retrieves messages for an enum field, including a message for each enum constant.
     *
     * @param classFieldMetadata Metadata representing the enum field.
     * @return The formatted messages for the enum and its constants.
     */
    private String getEnumMessages(ClassFieldMetadata classFieldMetadata) {
        String baseMessage = getMessageForCode(classFieldMetadata.packageClassName() + "." + classFieldMetadata.name(), classFieldMetadata.name());

        List<String> enumMessages = new ArrayList<>();
        for (Object enumConstant : classFieldMetadata.type().getEnumConstants()) {
            String constantName = ((Enum<?>) enumConstant).name();
            String constantMessage = getMessageForCode(classFieldMetadata.type().getName() + "." + constantName, null);
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
     * Resolves a message from an expression that may contain a message key.
     * Supports direct messages and message keys in the format {key}.
     *
     * <p>Examples:</p>
     * - "{user.email.description}" → looks up message for key "user.email.description"
     * - "Direct message" → returns "Direct message" unchanged
     *
     * @param expression message or message key expression
     * @param defaultMessage fallback message if key is not found
     * @return resolved message or default message
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
