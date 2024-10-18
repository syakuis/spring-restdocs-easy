package com.github.syakuis.spring.restdocs.easy.core;

import lombok.Builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-06-16
 */
@Builder
public record DataClassMetadata(
    String packageName,
    String className,
    String fieldName,
    Class<?> fieldType,
    Class<?> target,
    Field field,
    Annotation[] annotations
) {
}
