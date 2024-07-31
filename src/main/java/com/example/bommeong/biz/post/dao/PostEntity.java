package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.user.domain.ShelterEntity;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", referencedColumnName = "shelter_id")
    private ShelterEntity shelter;

    @Formula("(SELECT u.name FROM user u WHERE u.id = shelter_id)")
    private String shelterName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_name")
    private String imageName;

    @Transient
    private MultipartFile uploadFile;

    // @Enumerated(EnumType.STRING)
    @Column
    private String status;

    @Column(name = "expected_euthanasia_date")
    private Date expectedEuthanasiaDate;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BomInfoEntity bomInfoEntity;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<AdoptEntity> adoptEntity;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeEntity> likes;


    public PostEntity(PostModel model) {
        this.postId = model.getPostId();
        this.shelter = ShelterEntity.builder().id(model.getShelterId()).build();
        this.shelterName = model.getShelterName();
        this.imageName = model.getImageName();
        this.imageUrl = model.getImageUrl();
        this.status = model.getStatus();
        this.expectedEuthanasiaDate = model.getExpectedEuthanasiaDate();
    }

    public PostEntity(LikeEntity entity) {
        this.postId = entity.getPost().getPostId();
    }

    @Override
    public PostModel toModel() { return new PostModel(this); }
}
