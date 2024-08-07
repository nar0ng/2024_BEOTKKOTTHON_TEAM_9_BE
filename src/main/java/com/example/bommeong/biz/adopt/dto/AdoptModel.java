package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationStatus;
import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import java.time.LocalDateTime;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdoptModel{
    private Long adoptId;
    private Long postId;
    private Long memberId;
    private String imageUrl;
    private String imageName;
    private MultipartFile uploadFile;
    private AdoptApplicationStatus status;
    protected LocalDateTime createdAt;

    public AdoptApplicationModel adoptApplication;

    public AdoptModel(AdoptEntity entity) {
        this.adoptId = entity.getAdoptId();
        this.memberId = entity.getUser().getId();
        this.postId = entity.getPost().getPostId();
        this.imageUrl = entity.getImageUrl();
        this.status = entity.getStatus();
        this.imageName = entity.getImageName();
        this.adoptApplication = entity.getAdoptApplicationEntity().toModel();
        this.createdAt = entity.getCreatedAt();
    }

    public AdoptEntity toEntity() { return new AdoptEntity(this); }

}
