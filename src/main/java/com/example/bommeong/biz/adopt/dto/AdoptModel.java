package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
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
    private String status;

    public AdoptApplicationModel adoptApplication;

    public AdoptModel(AdoptEntity entity) {
        this.adoptId = entity.getAdoptId();
        this.memberId = entity.getUser().getId();
        this.postId = entity.getPostEntity().getPostId();
        this.imageUrl = entity.getImageUrl();
        this.status = entity.getStatus();
        this.imageName = entity.getImageName();
        this.adoptApplication = entity.getAdoptApplicationEntity().toModel();
    }

    public AdoptEntity toEntity() { return new AdoptEntity(this); }

}
