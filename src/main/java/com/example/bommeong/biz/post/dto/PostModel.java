package com.example.bommeong.biz.post.dto;

import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.common.dto.BaseModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PostModel extends BaseModel {
    private Long postId;
    private Long shelterId;
    private String shelterName;
    private String imageUrl;
    private String imageName;
    private MultipartFile uploadFile;
    private String status;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BomInfoModel bomInfo;

    public PostModel(PostEntity entity) {

    }

    @Override
    public PostEntity toEntity() { return new PostEntity(this); }

}
