package com.example.bommeong.biz.user.dto;

import lombok.Builder;

@Builder
public record AdoptionStatusDto(
        int totalDogsCount,
        int todayAdoptionRequests,
        int completedAdoptions,
        int pendingAdoptions
) {
}
