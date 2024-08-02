package com.example.bommeong.biz.post.dao;

import lombok.Getter;

@Getter
public enum PostStatus {
    BEFORE("진행중"),
    COMPLETED("완료");

    private final String description;

    PostStatus(String description) {this.description = description;}
}
