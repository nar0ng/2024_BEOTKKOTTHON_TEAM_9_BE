package com.example.bommeong.biz.adopt.dao;

import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "adopt")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class AdoptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_id")
    private Long adoptId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", nullable = false)
    private PostEntity post;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", unique = true, nullable = false)
    private UserEntity user;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "status")
    private String status;

    protected LocalDateTime createdAt;

    @OneToOne(mappedBy = "adoptEntity", cascade = CascadeType.ALL)
    private AdoptApplicationEntity AdoptApplicationEntity;

    public AdoptEntity(AdoptModel model) {
        this.adoptId = model.getAdoptId();
        this.user = UserEntity.builder().id(model.getMemberId()).build();
        PostEntity post = new PostEntity();
        post.setPostId(model.getPostId());
        this.post = post;
        this.status = model.getStatus();
        this.AdoptApplicationEntity = model.getAdoptApplication().toEntity();
        this.createdAt = model.getCreatedAt();
    }

    public AdoptModel toModel() { return new AdoptModel(this); }

}
