package com.example.bommeong.biz.adopt.dto;

import groovy.transform.builder.Builder;

@Builder
public record AdoptApplicantDto(
        Long memberId,
        String email,
        String name,
        String reasonForAdoption
) {
}
