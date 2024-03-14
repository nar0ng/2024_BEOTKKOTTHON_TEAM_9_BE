package com.example.bommeong.biz.content.dto;

import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Entity
@Table(name = "History")
@ToString
@DynamicInsert
@NoArgsConstructor
public class HistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "content_id")
    private Long contentId;

    @Transient
    private Integer count;

    @Transient
    private String title;



    public HistoryEntity(HistoryModel model) {
        this.historyId = model.getHistoryId();
        this.contentId = model.getContentId();
        this.title = model.getTitle();
    }

    public HistoryEntity(Long contentId) {
        this.contentId = contentId;
    }
    public HistoryEntity(Long contentId, String title) {
        this.contentId = contentId;
        this.title = title;
    }
    @Override
    public HistoryModel toModel() { return new HistoryModel(this); }
}
