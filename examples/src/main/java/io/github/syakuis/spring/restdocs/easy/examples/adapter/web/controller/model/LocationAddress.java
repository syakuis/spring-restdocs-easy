package io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
public record LocationAddress(
    String address,
    String detailAddress,
    String zipCode
) {
}
