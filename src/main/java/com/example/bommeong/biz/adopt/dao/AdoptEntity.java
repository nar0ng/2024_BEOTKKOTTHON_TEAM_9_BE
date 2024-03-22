package com.example.bommeong.biz.adopt.dao;

import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.multipart.MultipartFile;

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
    private PostEntity postEntity;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "status")
    private String status;

    @OneToOne(mappedBy = "adoptEntity", cascade = CascadeType.ALL)
    private AdoptApplicationEntity AdoptApplicationEntity;

    public AdoptEntity(AdoptModel model) {
        this.adoptId = model.getAdoptId();
        this.user = User.builder().id(model.getMemberId()).build();
        PostEntity post = new PostEntity();
        post.setPostId(model.getPostId());
        this.postEntity = post;
        this.status = model.getStatus();
    }

    public AdoptModel toModel() { return new AdoptModel(this); }

}
