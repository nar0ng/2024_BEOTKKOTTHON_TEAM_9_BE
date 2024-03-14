package com.example.bommung.biz.content.dto;

import com.example.bommung.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "tag")
@ToString
@NoArgsConstructor
public class TagEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @JoinColumn(name = "page_id", referencedColumnName = "page_id")
    @ManyToOne
    private ContentPageEntity contentPageEntity;

    @Column(name = "tag_name")
    private String tagName;

    public TagEntity(TagModel model) {
        this.tagId = model.getTagId();
        ContentPageModel e = new ContentPageModel();
        e.setContentId(model.getPageId());
        this.contentPageEntity = new ContentPageEntity(e);
        this.tagName = model.getTageName();
    }

    @Override
    public TagModel toModel() { return new TagModel(this); }
}
