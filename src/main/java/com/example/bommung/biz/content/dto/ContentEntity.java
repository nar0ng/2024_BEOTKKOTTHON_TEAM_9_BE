package com.example.bommung.biz.content.dto;

import com.example.bommung.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "content")
@ToString
@DynamicInsert
@NoArgsConstructor
public class ContentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "user_id")
    private Long userId;

    @Transient
    @Formula("(SELECT u.user_name FROM user u WHERE u.user_id = user_id)")
    private String userName;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_img")
    private String companyImg;

    @Column(name = "company_img_name")
    private String companyImgName;

    @Transient
    private MultipartFile uploadFile;

    @Column(name="deadline")
    private LocalDateTime deadLine;

    @Column(name = "short_yn")
    private String shortYn;

    @Column(name = "view_yn")
//    @ColumnDefault("Y") //테스트
    @ColumnDefault("N") //배포
    private String viewYn;

    @OneToMany(mappedBy = "contentEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("page_order ASC")
    private final List<ContentPageEntity> contentPageEntityList = new ArrayList<>();


    public ContentEntity(ContentModel model) {
        this.contentId = model.getContentId();
        this.userId = model.getUserId();
        this.userName = model.getUserName();
        this.title = model.getTitle();
        this.description = model.getDescription();
        this.category = model.getCategory();
        this.viewCount = model.getViewCount();
        this.companyName = model.getCompanyName();
        this.companyImg = model.getCompanyImg();
        this.companyImgName = model.getCompanyImgName();
        this.deadLine = model.getDeadLine();
        this.shortYn = model.getShortYn();
        this.viewYn = model.getViewYn();

        this.contentPageEntityList.addAll(
                model.getContentPageList().stream().map(ContentPageModel::toEntity).toList()
        );
    }

    @Override
    public ContentModel toModel() { return new ContentModel(this); }
}
