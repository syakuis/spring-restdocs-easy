package com.github.syakuis.spring.restdocs.easy.generate;

import java.lang.reflect.Field;

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-28
 */
public enum MessageFormatter {
    UNDERSCORE_FIELD_NAME {
        @Override
        public String format(Class<?> objectType, Field field){
            return "_." + field.getName();
        }
    },
    FIELD_NAME {
        @Override
        public String format(Class<?> objectType, Field field){
            return field.getName();
        }
    }, CLASS_NAME {
        @Override
        public String format(Class<?> objectType, Field field){
            return objectType.getSimpleName() + "." + field.getName();
        }
    }, PACKAGE {
        @Override
        public String format(Class<?> objectType, Field field){
            return objectType.getName() + "." + field.getName();
        }
    }, NONE {
        @Override
        public String format(Class<?> objectType, Field field){
            return field.getName();
        }
    };

    public abstract String format(Class<?> objectType, Field field);
}
