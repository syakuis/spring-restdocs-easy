package com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller;

import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.Job;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.LocationAddress;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberRequest;
import com.github.syakuis.spring.restdocs.easy.examples.adapter.web.controller.model.MemberResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Seok Kyun. Choi.
 * @since 2024-10-22
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
@Validated
public class MemberRestController {
    @GetMapping
    List<MemberResponse> list(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "age", required = false) Integer age,
        @RequestParam(name = "job", required = false) Job job) {
        return List.of(
            new MemberResponse(699L, "Name_wusha", 81, Job.ENGINEER, "email_bdifzyt@example.com", true,
                List.of("tag_urx", "tag_rvq", "tag_pta", "tag_jtp"),
                new LocationAddress("Street_yhgux", "City_yrbqp", "Country_lzuzr")),
            new MemberResponse(212L, "Name_nosap", 36, Job.ENGINEER, "email_dzqftnw@example.com", true,
                List.of("tag_ryr", "tag_uqs", "tag_uou", "tag_ueq", "tag_moe"),
                new LocationAddress("Street_aruhj", "City_ndkmc", "Country_ghckh"))
        );
    }

    @GetMapping("/{id}")
    MemberResponse view(@PathVariable("id") long id) {
        return new MemberResponse(id, "Name_muzsn", 40, Job.ENGINEER, "email_vwhvdhl@example.com", false,
            List.of("tag_fjv", "tag_hyk", "tag_enr", "tag_qtp"),
            new LocationAddress("Street_qqsxz", "City_iidcg", "Country_ylsoa"));
    }

    @PostMapping
    MemberResponse register(@Valid @RequestBody MemberRequest request) {
        return new MemberResponse(
            1L,
            request.name(),
            request.age(),
            request.job(),
            request.email(),
            request.active(),
            request.tags(),
            request.locationAddress()
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable("id") long id, @Valid @RequestBody MemberRequest request) {
    }

    @PatchMapping(path = "/{id}/attributes/name", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateName(@PathVariable("id") long id, @NotBlank @RequestBody String name) {
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remove(@PathVariable("id") long id) {
    }
}
