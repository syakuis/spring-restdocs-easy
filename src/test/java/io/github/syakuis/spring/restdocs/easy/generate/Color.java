package io.github.syakuis.spring.restdocs.easy.generate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-21
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Color {
    private String background;
    @NotNull
    private String font;
}
