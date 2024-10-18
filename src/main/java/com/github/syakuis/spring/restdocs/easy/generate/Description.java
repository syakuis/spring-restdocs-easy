package com.github.syakuis.spring.restdocs.easy.generate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 * @author Seok Kyun. Choi.
 * @since 2023-07-15
 */
@Getter
@Accessors(fluent = true)
@Builder
@ToString
public class Description {
    private final String name;

    @Builder.Default
    private final JsonFieldType type = JsonFieldType.STRING; // 기본값 설정

    private final Object desc;

    @Builder.Default
    private final boolean optional = false; // 기본값 설정

    @Builder.Default
    private final boolean ignore = false; // 기본값 설정
}