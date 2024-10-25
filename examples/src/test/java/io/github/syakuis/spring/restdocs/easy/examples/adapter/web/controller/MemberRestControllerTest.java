package io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.spring.restdocs.easy.configuration.AutoConfigureMvcRestDocs;
import io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.Job;
import io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.LocationAddress;
import io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberRequest;
import io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberResponse;
import io.github.syakuis.spring.restdocs.easy.generate.RestDocs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
@WebMvcTest(MemberRestController.class)
@AutoConfigureMvcRestDocs
class MemberRestControllerTest {
    private final String RESTDOCS_PATH = "members/{method-name}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestDocs restDocs;


    @Test
    void list() throws Exception {
        mockMvc.perform(get("/members")
                .param("name", "stela")
                .param("age", "10")
                .param("job", "ENGINEER")
                .param("email", "email@email.com")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document(RESTDOCS_PATH,
                restDocs.headers().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON).requestHeaders(),
                restDocs.headers().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).responseHeaders(),

                restDocs.params()
                    .add("name", "name parameter")
                    .add("age", "{io.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberRequest.age}")
                    .add("job", restDocs.generate(Job.class).join())
                    .add("email", "{email}")
                    .queryParameters(),

                restDocs.generate("[].", MemberResponse.class)
                    .addAll("[].locationAddress.", LocationAddress.class)
                    .responseFields()
            ));
    }

    @Test
    void view() throws Exception {
        mockMvc.perform(get("/members/{id}", 953))
            .andExpect(status().isOk())
            .andDo(document(RESTDOCS_PATH,
                restDocs.params().add("id", "id").pathParameters(),

                restDocs.generate(MemberResponse.class).responseFields()
                    .andWithPrefix("locationAddress.", restDocs.generate(LocationAddress.class).toField())

                ))
        ;
    }

    @Test
    void register() throws Exception {
        MemberRequest request = new MemberRequest("John Doe", 30, Job.ACCOUNTANT, "john@example.com", true,
            List.of("tag1", "tag2"), new LocationAddress("123 Main St", "City", "Country"));

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document(RESTDOCS_PATH,
                restDocs.headers().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .add(HttpHeaders.AUTHORIZATION, "{http.headers.authorization.BASIC_AUTHORIZATION}")
                    .requestHeaders(),

                restDocs.generate(MemberRequest.class).requestFields()
                    .andWithPrefix("locationAddress.", restDocs.generate(LocationAddress.class).toField()),

                restDocs.generate(MemberResponse.class).responseFields()
                    .andWithPrefix("locationAddress.", restDocs.generate(LocationAddress.class).toField())
            ));
    }

    @Test
    void update() throws Exception {
        MemberRequest request = new MemberRequest("John Doe", 30, Job.ENGINEER, "john@example.com", true,
            List.of("tag1", "tag2"), new LocationAddress("123 Main St", "City", "Country"));

        mockMvc.perform(put("/members/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent())
            .andDo(document(RESTDOCS_PATH,
                restDocs.generate(MemberRequest.class).requestFields()
                    .andWithPrefix("locationAddress.", restDocs.generate(LocationAddress.class).toField())
            ));
    }

    @Test
    void updateName() throws Exception {
        mockMvc.perform(patch("/members/{id}/attributes/name", 1)
                .contentType(MediaType.TEXT_PLAIN)
                .content("Updated Name"))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(delete("/members/{id}", 1))
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}