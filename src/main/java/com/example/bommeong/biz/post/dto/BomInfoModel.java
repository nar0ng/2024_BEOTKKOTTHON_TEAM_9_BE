package com.example.bommeong.biz.post.dto;

import com.example.bommeong.biz.post.dao.BomInfoEntity;
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
public class BomInfoModel extends BaseModel {
    private Long infoId;
    private Long postId;
    private String name;
    private String age;
    private String breed;
    private String gender;
    private String personality;
    private String extra;
    public BomInfoModel(BomInfoEntity entity) {}

    @Override
    public BomInfoEntity toEntity() { return new BomInfoEntity(this);}
}
