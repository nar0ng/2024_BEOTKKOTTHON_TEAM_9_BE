package com.example.bommung.biz.content.dto;

import com.example.bommung.common.dto.BaseModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ContentPageModel extends BaseModel {
    private Long pageId;
    private Long contentId;
    private MultipartFile uploadFile;
    private String url;
    private Long size;
    private String originalName;
    private String description;
    private Integer pageOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> tagList = new ArrayList<>();
//    private List<TagModel> tagModelList = new ArrayList<>();

    public ContentPageModel(ContentPageEntity entity) {
        this.pageId = entity.getPageId();
        this.contentId = entity.getContentEntity().getContentId();
        this.uploadFile = entity.getUploadFile();
        this.url = entity.getUrl();
        this.size = entity.getSize();
        this.originalName = entity.getOriginalName();
        this.description = entity.getDescription();
        this.pageOrder = entity.getPage_order();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.tagList.addAll(Arrays.asList(entity.getTagList().split(",")));
//        this.tagModelList.addAll(
//                entity.getTagEntityList().stream().map(TagEntity::toModel).toList()
//        );
    }

    @Override
    public ContentPageEntity toEntity() { return new ContentPageEntity(this); }
}
