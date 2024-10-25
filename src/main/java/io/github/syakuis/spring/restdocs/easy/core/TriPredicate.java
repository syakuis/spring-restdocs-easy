package io.github.syakuis.spring.restdocs.easy.core;

/**
 * Represents a predicate (boolean-valued function) of three arguments for "Spring REST Docs Easy".
 * Extends the concept of {@link java.util.function.BiPredicate} to support three parameters.
 *
 * <p>This is a functional interface whose functional method is {@link #test(Object, Object, Object)}.</p>
 *
 * <p>Example usage in "Spring REST Docs Easy":</p>
 * <pre>{@code
 * // Checking getter method validity
 * TriPredicate<Class<?>, String, Class<?>> isGetter = (targetClass, fieldName, fieldType) -> {
 *     String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
 *     try {
 *         Method method = targetClass.getMethod(methodName);
 *         return method.getReturnType().equals(fieldType);
 *     } catch (NoSuchMethodException e) {
 *         return false;
 *     }
 * };
 * }</pre>
 *
 * @author Seok Kyun. Choi.
 * @since 2024-06-02
 *
 * @param <A> the type of the first argument ({@code typically a Class<?>})
 * @param <B> the type of the second argument (typically a String)
 * @param <C> the type of the third argument ({@code typically a Class<?>})
 *
 * @see java.util.function.Predicate
 * @see java.util.function.BiPredicate
 */
@FunctionalInterface
public interface TriPredicate<A, B, C> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param a first input argument
     * @param b second input argument
     * @param c third input argument
     * @return true if the input matches the predicate, otherwise false
     */
    boolean test(A a, B b, C c);
}