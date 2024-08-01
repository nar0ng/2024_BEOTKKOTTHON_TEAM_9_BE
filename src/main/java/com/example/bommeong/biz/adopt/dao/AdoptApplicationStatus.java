package com.example.bommeong.biz.adopt.dao;

import lombok.Getter;

@Getter
public enum AdoptApplicationStatus {
    PENDING("대기중"),
    REJECTED("거절"),
    PASSED("승인");

    private final String description;

    AdoptApplicationStatus(String description) {
        this.description = description;
    }


}
