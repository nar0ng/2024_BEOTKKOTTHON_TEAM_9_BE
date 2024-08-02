package com.example.bommeong.biz.post.dto;

import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dao.PostStatus;
import com.example.bommeong.common.dto.BaseModel;
import java.util.Date;
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
    private PostStatus status;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    private String expectedEuthanasiaDate;
    public BomInfoModel bomInfo;

    @Data
    @NoArgsConstructor
    public static class PostList {
        private Long postId;
        private Long shelterId;
        private String shelterName;
        private String imageUrl;
        private PostStatus status;
        protected LocalDateTime createdAt;
        protected String expectedEuthanasiaDate;
        public BomInfoModel bomInfo;

        public PostList(PostEntity entity) {
            this.postId = entity.getPostId();
            this.shelterId = entity.getShelter().getId();
            this.shelterName = entity.getShelterName();
            this.imageUrl = entity.getImageUrl();
            this.status = entity.getStatus();
            this.createdAt = entity.getCreatedAt();
            this.expectedEuthanasiaDate = entity.getExpectedEuthanasiaDate().toString();
            this.bomInfo = entity.getBomInfoEntity().toModel();
        }
    }

    public PostModel(PostEntity entity) {
        this.postId = entity.getPostId();
        this.imageUrl = entity.getImageUrl();
        this.shelterId = entity.getShelter().getId();
        this.shelterName = entity.getShelterName();
        this.createdAt = entity.getCreatedAt();
        this.status = entity.getStatus();
        this.expectedEuthanasiaDate = entity.getExpectedEuthanasiaDate().toString();
        this.bomInfo = entity.getBomInfoEntity().toModel();
    }

    @Override
    public PostEntity toEntity() { return new PostEntity(this); }

}
