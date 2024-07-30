package com.example.bommeong.biz.adopt.dto;

import groovy.transform.builder.Builder;

@Builder
public record AdoptApplicantDetailsDto(
        Long memberId,
        String email,
        String name,
        AdoptApplicationModel application
) {
}
