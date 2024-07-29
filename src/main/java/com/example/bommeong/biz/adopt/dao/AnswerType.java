package com.example.bommeong.biz.adopt.dao;

import lombok.Getter;

@Getter
public enum AnswerType {
    YES("예"),
    NO("아니오"),
    AGREE("모두 찬성"),
    PARTIALLY_AGREE("부분 찬성"),
    DISAGREE("모두 반대");

    private final String value;

    AnswerType(String value) {
        this.value = value;
    }
}
