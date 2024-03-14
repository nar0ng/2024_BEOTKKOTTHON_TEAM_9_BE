package com.example.bommung.biz.content.dto;

import com.example.bommung.biz.content.repository.ContentEntityInterface;
import com.example.bommung.common.dto.BaseModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ContentModel extends BaseModel {
    private Long contentId;
    private Long userId;
    private String userName;
    private String title;
    private String description;
    private String category;
    private int viewCount;
    private String companyName;
    private MultipartFile uploadFile;
    private String companyImg;
    private String companyImgName;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadLine;
    private String shortYn;
    private String viewYn;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public final List<ContentPageModel> contentPageList = new ArrayList<>();

    public ContentModel(ContentEntity entity) {
        this.contentId = entity.getContentId();
        this.userId = entity.getUserId();
        this.userName = entity.getUserName();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.category = entity.getCategory();
        this.viewCount = entity.getViewCount();
        this.companyName = entity.getCompanyName();
        this.uploadFile = entity.getUploadFile();
        this.companyImg = entity.getCompanyImg();
        this.companyImgName = entity.getCompanyImgName();
        this.deadLine = entity.getDeadLine();
        this.shortYn = entity.getShortYn();
        this.viewYn = entity.getViewYn();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.contentPageList.addAll(
                entity.getContentPageEntityList().stream().map(ContentPageEntity::toModel).toList()
        );
    }

    public ContentModel(ContentEntityInterface entityInterface) {
        this.contentId = entityInterface.getContentId();
        this.userId = entityInterface.getUserId();
        this.title = entityInterface.getTitle();
        this.description = entityInterface.getDescription();
        this.category = entityInterface.getCategory();
        this.viewCount = entityInterface.getViewCount();
        this.companyName = entityInterface.getCompanyName();
        this.companyImg = entityInterface.getCompanyImg();
        this.deadLine = entityInterface.getDeadLine();
        this.shortYn = entityInterface.getShortYn();
        this.viewYn = entityInterface.getViewYn();
    }

    public ContentModel onlyContent(ContentModel model) {
        ContentModel model1 = new ContentModel();
        model1.setContentId(model.getContentId());
        model1.setUserId(model.getUserId());
        model1.setTitle(model.getTitle());
        model1.setDescription(model.getDescription());
        model1.setCategory(model.getCategory());
        model1.setViewCount(model.getViewCount());
        model1.setCompanyName(model.getCompanyName());
        model1.setDeadLine( model.getDeadLine());
        model1.setShortYn(model.getShortYn());
        model1.setViewYn(model.getViewYn());
        return model1;
    }

    @Override
    public ContentEntity toEntity() { return new ContentEntity(this); }
}
