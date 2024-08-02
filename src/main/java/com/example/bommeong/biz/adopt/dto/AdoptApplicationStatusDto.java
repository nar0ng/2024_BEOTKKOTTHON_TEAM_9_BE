package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationStatus;

public record AdoptApplicationStatusDto(
        Long postId,
        Long memberId,
        AdoptApplicationStatus adoptApplicationStatus
) {
}
