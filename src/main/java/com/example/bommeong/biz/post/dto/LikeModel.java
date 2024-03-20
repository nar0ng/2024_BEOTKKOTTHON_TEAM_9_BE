package com.example.bommeong.biz.post.dto;

import com.example.bommeong.biz.post.dao.LikeEntity;
import com.example.bommeong.common.dto.BaseModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LikeModel extends BaseModel {
    private Long likeId;
    private Long memberId;
    private Long postId;
    private String flag;


    public LikeModel(LikeEntity entity) {}

    @Override
    public LikeEntity toEntity() { return new LikeEntity(this); }
}
