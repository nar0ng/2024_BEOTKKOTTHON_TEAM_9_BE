package com.example.bommeong.biz.user.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BomListDto(
        Long postId,
        String name,
        String breed,
        String gender,
        String extra,
        LocalDateTime createdAt,
        int adoptStatusCount
) {
}
