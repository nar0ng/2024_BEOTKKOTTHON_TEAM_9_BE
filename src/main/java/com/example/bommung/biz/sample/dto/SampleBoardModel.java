package com.example.bommung.biz.sample.dto;

import com.example.bommung.common.dto.BaseModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleBoardModel extends BaseModel {

    private Long boardNo;
    private String boardName;
    private String ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SampleBoardModel(SampleBoardEntity entity)  {
        log.debug("# SampleBoardEntity : {}", entity);
        this.boardNo = entity.getBoardNo();
        this.boardName = entity.getBoardName();
        this.ownerId = entity.getOwnerId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    @Override
    public SampleBoardEntity toEntity() {
        return new SampleBoardEntity(this);
    }
}
