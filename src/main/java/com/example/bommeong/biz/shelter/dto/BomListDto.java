package com.example.bommeong.biz.shelter.dto;

public record BomListDto(
        Long PostId,
        String name,
        String breed,
        String gender,
        String extra
        //LocalDateTime createdAt,
        //int adoptStatusCount
) {
}
