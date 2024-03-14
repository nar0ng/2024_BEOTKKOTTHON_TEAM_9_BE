package com.example.bommung.biz.content.dto;

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
public class TagModel extends BaseModel {
    private Long tagId;
    private Long pageId;
    private String tageName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TagModel(TagEntity entity) {
        this.tagId = entity.getTagId();
        this.pageId = entity.getContentPageEntity().getPageId();
        this.tageName = entity.getTagName();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public TagModel(String tageName) {
        this.tageName = tageName;
    }
    @Override
    public TagEntity toEntity() { return new TagEntity(this); }
}
