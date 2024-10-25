package io.github.syakuis.spring.restdocs.easy.generate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-23
 */
@ExtendWith(MockitoExtension.class)
class DefaultDescriptorsGeneratorTest {
    @Mock
    private MessageSource messageSource;

    @Test
    @DisplayName("이름과 설명으로 Descriptor를 생성한다")
    void add_WithNameAndDescription() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);

        // when
        generator.add("name", "description");
        List<Descriptor> descriptors = generator.generate().toList();

        // then
        assertThat(descriptors).hasSize(1);
        Descriptor descriptor = descriptors.getFirst();
        assertThat(descriptor.name()).isEqualTo("name");
        assertThat(descriptor.description()).isEqualTo("description");
        assertThat(descriptor.type()).isEqualTo(JsonFieldType.STRING);
        assertThat(descriptor.optional()).isFalse();
    }

    @Test
    @DisplayName("이름, 설명, 타입으로 Descriptor를 생성한다")
    void add_WithNameDescriptionAndType() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);

        // when
        generator.add("name", "description", JsonFieldType.NUMBER);
        List<Descriptor> descriptors = generator.generate().toList();

        // then
        assertThat(descriptors).hasSize(1);
        Descriptor descriptor = descriptors.getFirst();
        assertThat(descriptor.name()).isEqualTo("name");
        assertThat(descriptor.description()).isEqualTo("description");
        assertThat(descriptor.type()).isEqualTo(JsonFieldType.NUMBER);
        assertThat(descriptor.optional()).isFalse();
    }

    @Test
    @DisplayName("모든 속성을 지정하여 Descriptor를 생성한다")
    void add_WithAllProperties() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);

        // when
        generator.add("name", "description", JsonFieldType.BOOLEAN, true);
        List<Descriptor> descriptors = generator.generate().toList();

        // then
        assertThat(descriptors).hasSize(1);
        Descriptor descriptor = descriptors.getFirst();
        assertThat(descriptor.name()).isEqualTo("name");
        assertThat(descriptor.description()).isEqualTo("description");
        assertThat(descriptor.type()).isEqualTo(JsonFieldType.BOOLEAN);
        assertThat(descriptor.optional()).isTrue();
    }

    @Test
    @DisplayName("미리 생성된 Descriptor를 추가한다")
    void add_WithPrebuiltDescriptor() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);
        Descriptor prebuiltDescriptor = Descriptor.builder()
            .name("name")
            .description("description")
            .type(JsonFieldType.ARRAY)
            .optional(true)
            .build();

        // when
        generator.add(prebuiltDescriptor);
        List<Descriptor> descriptors = generator.generate().toList();

        // then
        assertThat(descriptors).hasSize(1);
        Descriptor descriptor = descriptors.getFirst();
        assertThat(descriptor.name()).isEqualTo("name");
        assertThat(descriptor.description()).isEqualTo("description");
        assertThat(descriptor.type()).isEqualTo(JsonFieldType.ARRAY);
        assertThat(descriptor.optional()).isTrue();
    }

    @Test
    @DisplayName("여러 개의 Descriptor를 추가할 수 있다")
    void add_MultipleDescriptors() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);

        // when
        generator.add("name1", "description1")
            .add("name2", "description2", JsonFieldType.NUMBER)
            .add("name3", "description3", JsonFieldType.BOOLEAN, true);

        List<Descriptor> descriptors = generator.generate().toList();

        // then
        assertThat(descriptors).hasSize(3);
        assertThat(descriptors).extracting("name")
            .containsExactlyInAnyOrder("name1", "name2", "name3");
    }

    @Test
    @DisplayName("name이 null이면 예외가 발생한다")
    void add_WithNullName() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);

        // when, then
        assertThatThrownBy(() -> generator.add(null, "description"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("name must not be null or blank");
    }

    @Test
    @DisplayName("name이 빈 문자열이면 예외가 발생한다")
    void add_WithBlankName() {
        // given
        DescriptorsGenerator generator = new DefaultDescriptorsGenerator(messageSource);

        // when, then
        assertThatThrownBy(() -> generator.add("  ", "description"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("name must not be null or blank");
    }
}