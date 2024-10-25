package com.github.syakuis.spring.restdocs.easy.configuration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-25
 */
class MessageSourceConfigurationTest {
    @Nested
    @SpringBootTest
    @Import(MessageSourceConfiguration.class)
    class UnReady {
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void test() {
            assertNotNull(applicationContext.getBean(MessageSource.class));
            assertNotNull(applicationContext.getBean(MessageSourceAccessor.class));
            assertNotNull(applicationContext.getBean(LocalValidatorFactoryBean.class));
            assertTrue(applicationContext.containsBeanDefinition("messageSourceAccessor"));
            assertTrue(applicationContext.containsBeanDefinition("validator"));
        }
    }

    @Nested
    @SpringBootTest
    @ContextConfiguration(classes = {Ready.Config.class, MessageSourceConfiguration.class})
    class Ready {
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void test() {
            assertNotNull(applicationContext.getBean(MessageSource.class));
            assertNotNull(applicationContext.getBean(MessageSourceAccessor.class));
            assertNotNull(applicationContext.getBean(LocalValidatorFactoryBean.class));
            assertFalse(applicationContext.containsBeanDefinition("messageSourceAccessor"));
            assertTrue(applicationContext.containsBeanDefinition("messageSourceAccessor2"));
            assertTrue(applicationContext.containsBeanDefinition("validator"));
        }

        @TestConfiguration
        static class Config {
            @Autowired
            private MessageSource messageSource;

            @Bean("messageSourceAccessor2")
            public MessageSourceAccessor messageSourceAccessor() {
                return new MessageSourceAccessor(messageSource, Locale.getDefault());
            }
        }
    }
}