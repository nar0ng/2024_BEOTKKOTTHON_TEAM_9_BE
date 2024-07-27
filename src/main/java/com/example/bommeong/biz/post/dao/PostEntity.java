package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.user.domain.ShelterEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
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
    @JoinColumn(name = "shelter_id", referencedColumnName = "id")
    private ShelterEntity shelterId;


    @Formula("(SELECT u.name FROM user u WHERE u.id = shelter_id)")
    private String shelterName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_name")
    private String imageName;

    @Transient
    private MultipartFile uploadFile;

    @Column
    private String status;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private BomInfoEntity bomInfoEntity;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<AdoptEntity> adoptEntity;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeEntity> likes = new HashSet<>();


    public PostEntity(PostModel model) {
        this.postId = model.getPostId();
        this.shelterId = ShelterEntity.builder().id(model.getShelterId()).build();
        this.shelterName = model.getShelterName();
        this.imageName = model.getImageName();
        this.imageUrl = model.getImageUrl();
        this.status = model.getStatus();
    }

    public PostEntity(LikeEntity entity) {
        this.postId = entity.getPost().getPostId();
    }

    @Override
    public PostModel toModel() { return new PostModel(this); }
}
