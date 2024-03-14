package com.example.bommung.biz.content.dto;

import com.example.bommung.biz.content.repository.HistoryEntityInterface;
import com.example.bommung.common.dto.BaseModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HistoryModel extends BaseModel {
    private Long historyId;
    private Long contentId;
    private String title;
    private LocalDateTime createdAt;

    public HistoryModel(HistoryEntity entity) {
        this.historyId = entity.getHistoryId();
        this.contentId = entity.getContentId();
        this.title = entity.getTitle();
        this.createdAt = entity.getCreatedAt();
    }

    public HistoryModel(HistoryEntityInterface entityInterface) {
        this.contentId = entityInterface.getContentId();
        this.title = entityInterface.getTitle();
    }


    @Override
    public HistoryEntity toEntity() { return new HistoryEntity(this); }
}
