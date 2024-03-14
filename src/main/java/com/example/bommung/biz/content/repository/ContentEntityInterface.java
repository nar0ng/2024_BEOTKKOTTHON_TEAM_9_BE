package com.example.bommung.biz.content.repository;

import java.time.LocalDateTime;

public interface ContentEntityInterface {
    Long getContentId();

    Long getUserId();

    String getTitle();
    String getDescription();
    String getCategory();
    Integer getViewCount();
    String getCompanyName();
    String getCompanyImg();
    LocalDateTime getDeadLine();
    String getShortYn();

    String getViewYn();
}
