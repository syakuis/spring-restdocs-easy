package com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.syakuis.spring.restdocs.easy.configuration.AutoConfigureMvcRestDocs;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.Job;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.LocationAddress;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberRequest;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberResponse;
import com.github.syakuis.spring.restdocs.easy.generate.RestDocs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
        mockMvc.perform(get("/members"))
            .andExpect(status().isOk())
            .andDo(document(RESTDOCS_PATH,
                restDocs.generate(MemberResponse.class).responseFields("[].")
                    .andWithPrefix("[].locationAddress.", restDocs.generate(LocationAddress.class).toField())
            ));
    }

    @Test
    void view() throws Exception {
        mockMvc.perform(get("/members/953"))
            .andExpect(status().isOk());
    }

    @Test
    void register() throws Exception {
        MemberRequest request = new MemberRequest("John Doe", 30, Job.ACCOUNTANT, "john@example.com", true,
            List.of("tag1", "tag2"), new LocationAddress("123 Main St", "City", "Country"));

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document(RESTDOCS_PATH,
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

        mockMvc.perform(put("/members/1")
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
        mockMvc.perform(patch("/members/1/attributes/name")
                .contentType(MediaType.TEXT_PLAIN)
                .content("Updated Name"))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(delete("/members/1"))
            .andExpect(status().isNoContent())
            .andDo(print());
    }
}