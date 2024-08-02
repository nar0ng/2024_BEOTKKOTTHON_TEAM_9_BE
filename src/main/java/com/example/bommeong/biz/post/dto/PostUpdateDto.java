package com.example.bommeong.biz.post.dto;
import groovy.transform.builder.Builder;
import lombok.Getter;

@Builder
public record PostUpdateDto(
        BomInfoModel bomInfoModel
) {

}