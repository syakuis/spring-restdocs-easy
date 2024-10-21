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
public class Descriptor {
    private final String name;

    @Builder.Default
    private final JsonFieldType type = JsonFieldType.STRING; // 기본값 설정

    private final Object description;

    /**
     * 필드가 필수적이지 않고 선택적임을 문서화한다.
     */
    @Builder.Default
    private final boolean optional = false; // 기본값 설정

    /**
     * 필드를 문서화에서 제외한다.
     */
    @Builder.Default
    private final boolean ignore = false; // 기본값 설정
}