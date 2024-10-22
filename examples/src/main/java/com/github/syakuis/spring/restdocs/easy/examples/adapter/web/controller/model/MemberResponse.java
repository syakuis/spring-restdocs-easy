package com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model;

import java.util.List;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
public record MemberResponse(
    long id,
    String name,
    int age,
    Job job,
    String email,
    boolean active,
    List<String> tags,
    LocationAddress locationAddress
) {
}
