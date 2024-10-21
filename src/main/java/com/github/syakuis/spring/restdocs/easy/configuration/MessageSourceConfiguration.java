package com.github.syakuis.spring.restdocs.easy.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

@RequiredArgsConstructor
@ConditionalOnBean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
@AutoConfiguration(after = MessageSource.class)
public class MessageSourceConfiguration {
    private final MessageSource messageSource;

    @ConditionalOnMissingBean(name = "messageSourceAccessor")
    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @ConditionalOnMissingBean(name = "validator")
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
