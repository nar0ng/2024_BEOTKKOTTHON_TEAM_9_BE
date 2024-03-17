package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.springframework.web.multipart.MultipartFile;

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
    private User shelterId;


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

    @OneToOne(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private BomInfoEntity bomInfoEntity;

    public PostEntity(PostModel model) {
        this.postId = model.getPostId();
        this.shelterId = User.builder().id(model.getShelterId()).build();
        this.shelterName = model.getShelterName();
        this.imageName = model.getImageName();
        this.imageUrl = model.getImageUrl();
        this.status = model.getStatus();
    }

    @Override
    public PostModel toModel() { return new PostModel(this); }
}