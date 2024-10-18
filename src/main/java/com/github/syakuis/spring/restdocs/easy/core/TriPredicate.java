package com.github.syakuis.spring.restdocs.easy.core;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-06-02
 */
@FunctionalInterface
public interface TriPredicate<A, B, C> {
    boolean test(A a, B b, C c);
}