package com.example.bommeong.biz.content.dto;

import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Entity
@Table(name = "content_page")
@ToString
@NoArgsConstructor
@DynamicInsert
public class ContentPageEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long pageId;

    @JoinColumn(name = "content_id", referencedColumnName = "content_id")
    @ManyToOne
    private ContentEntity contentEntity;

    @Column(name = "url")
    private String url;

    @Column(name = "size")
    private Long size;

    @Column(name = "originalName")
    private String originalName;

    @Column(name = "description")
    private String description;

    @Column(name = "page_order")
    private Integer page_order;

    @Transient
    private MultipartFile uploadFile;

    @Column(name = "tag_list")
    private String tagList;

//    @OneToMany(mappedBy = "contentPageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private final List<TagEntity> tagEntityList = new ArrayList<>();

    public ContentPageEntity(ContentPageModel model) {
        this.pageId = model.getPageId();

        ContentModel e = new ContentModel();
        e.setContentId(model.getContentId());

        ContentEntity contentEntity1 = new ContentEntity();
        contentEntity1.setContentId(model.getContentId());

        this.contentEntity = contentEntity1;
        this.uploadFile = model.getUploadFile();
        this.url = model.getUrl();
        this.size = model.getSize();
        this.originalName = model.getOriginalName();
        this.description = model.getDescription();
        this.page_order = model.getPageOrder();
        StringBuilder sb = new StringBuilder();
        for (String item : model.getTagList()) {
            sb.append(item).append(", ");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        this.tagList = sb.toString();
//        this.tageEntityList.addAll(
//          model.getTagModelList().stream().map(TagModel::toEntity).toList()
//        );
//        this.tagEntityList.addAll(
//          model.getTagList().stream().map(tag -> new TagModel(tag).toEntity()).toList()
//        );
    }

    @Override
    public ContentPageModel toModel() { return new ContentPageModel(this); }
}
