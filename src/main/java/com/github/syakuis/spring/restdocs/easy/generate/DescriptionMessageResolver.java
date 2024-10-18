package com.github.syakuis.spring.restdocs.easy.generate;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-01-12
 */
public class DescriptionMessageResolver {
    private static final Pattern EXPRESSION_MESSAGE_PATTERN = Pattern.compile("^\\{(.*)\\}$");
    private final MessageSource messageSource;
    private List<MessageFormatter> messageFormatters = List.of(MessageFormatter.PACKAGE, MessageFormatter.CLASS_NAME, MessageFormatter.UNDERSCORE_FIELD_NAME, MessageFormatter.FIELD_NAME);

    public DescriptionMessageResolver(MessageSource messageSource, List<MessageFormatter> messageFormatters) {
        this.messageSource = messageSource;
        if (messageFormatters != null) this.messageFormatters = messageFormatters;
    }

    public String resolve(Class<?> objectType, Field field) {
        String code = MessageFormatter.NONE.format(objectType, field);
        String enumDesc = "";

        if (Enum.class.isAssignableFrom(field.getType())) {
            enumDesc = "\n\n" + getEnumDescription(field.getType()) + "\n\n";
        }

        if (messageSource == null) {
            return code + enumDesc;
        }

        for (var messageCodeFormatter : messageFormatters) {
            try {
                return getMessage(messageCodeFormatter.format(objectType, field)) + enumDesc;
            } catch (NoSuchMessageException e) {

            }
        }

        return code + enumDesc;
    }

    private String getEnumDescription(Class<?> objectType) {
        Object[] enumConstants = objectType.getEnumConstants();
        List<String> descriptions = new ArrayList<>();
        for (Object enumConstant : enumConstants) {
            var name = ((Enum<?>) enumConstant).name();
            var message = getMessage(objectType.getName() + "." + name, null);
            var description = message != null ? name + " : " + message : name;

            descriptions.add(description);
        }


        return String.join(" + \n", descriptions);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    private String getMessage(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, Locale.getDefault());
    }

    // todo 개발중...
    private String getRegxMessage(String code) {
        if (EXPRESSION_MESSAGE_PATTERN.matcher(code).matches()) {
            return messageSource.getMessage(code.replaceAll(EXPRESSION_MESSAGE_PATTERN.pattern(), "$1"),null, Locale.getDefault());
        }

        return code;
    }
}
