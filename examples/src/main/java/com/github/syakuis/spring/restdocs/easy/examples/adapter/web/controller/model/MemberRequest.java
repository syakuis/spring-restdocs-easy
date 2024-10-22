package com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
public record MemberRequest(
    @NotBlank
    String name,
    @Min(18)
    int age,
    @NotNull
    Job job,
    String email,
    boolean active,
    List<String> tags,
    LocationAddress locationAddress
) {
}
