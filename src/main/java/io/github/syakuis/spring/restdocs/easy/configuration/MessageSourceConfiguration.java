package io.github.syakuis.spring.restdocs.easy.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

@RequiredArgsConstructor
@AutoConfiguration(after = MessageSource.class)
public class MessageSourceConfiguration {
    private final MessageSource messageSource;

    @ConditionalOnMissingBean(MessageSourceAccessor.class)
    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @ConditionalOnMissingBean(LocalValidatorFactoryBean.class)
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
