package com.github.syakuis.spring.restdocs.easy.generate;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
@ExtendWith(MockitoExtension.class)
class DefaultRestDocsTest {

    @Mock
    private MessageSource messageSource;

    private DefaultRestDocs defaultRestDocs;

    @BeforeEach
    void setUp() {
        defaultRestDocs = new DefaultRestDocs(messageSource, new JsonFieldTypeMapper());
    }

    @Test
    void testGenerate() {
        RestDocs.Operator operator = defaultRestDocs.generate(TestClass.class);
        assertNotNull(operator);
        assertInstanceOf(DefaultRestDocs.DefaultOperator.class, operator);
    }

    @Test
    void testDefaultOperatorFilter() {
        RestDocs.Operator operator = defaultRestDocs.generate(TestClass.class);
        List<FieldDescriptor> fields = operator.filter("field1", "field2").toField();
        assertEquals(2, fields.size());
        assertTrue(fields.stream().anyMatch(fd -> fd.getPath().equals("field1")));
        assertTrue(fields.stream().anyMatch(fd -> fd.getPath().equals("field2")));
    }

    @Test
    void testDefaultOperatorExclude() {
        RestDocs.Operator operator = defaultRestDocs.generate(TestClass.class);
        List<FieldDescriptor> fields = operator.exclude("field3").toField();
        assertFalse(fields.stream().anyMatch(fd -> fd.getPath().equals("field3")));
    }

    @Test
    void testDefaultOperatorIgnore() {
        RestDocs.Operator operator = defaultRestDocs.generate(TestClass.class);
        List<FieldDescriptor> fields = operator.ignore("field1").toField();
        assertTrue(fields.stream().anyMatch(fd -> fd.getPath().equals("field1") && fd.isIgnored()));
    }

    @Test
    void testDefaultOperatorOptional() {
        RestDocs.Operator operator = defaultRestDocs.generate(TestClass.class);
        List<ParameterDescriptor> params = operator.optional("field1").toParameter();
        assertTrue(params.stream().anyMatch(pd -> pd.getName().equals("field1") && pd.isOptional()));
    }

    @Test
    void testDefaultOperatorToHeader() {
        RestDocs.Operator operator = defaultRestDocs.generate(TestClass.class);
        List<HeaderDescriptor> headers = operator.toHeader();
        assertFalse(headers.isEmpty());
    }

    // 테스트용 더미 클래스
    @Getter
    static class TestClass {
        private String field1;
        private String field2;
        private String field3;
    }
}