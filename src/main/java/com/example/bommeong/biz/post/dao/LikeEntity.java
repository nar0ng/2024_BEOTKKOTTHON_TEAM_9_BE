package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.post.dto.LikeModel;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
public class LikeEntity extends BaseEntity {

    @EmbeddedId
    private LikeId likeId;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private UserEntity user;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public LikeEntity(LikeModel model) {

    }

    // embeddedId 도 설정해줘야 entity 구성이 됨
    public LikeEntity(UserEntity user, PostEntity post) {
        this.likeId = new LikeId(user.getId(), post.getPostId());
        this.user = user;
        this.post = post;
    }

    @Override
    public LikeModel toModel() { return new LikeModel(this); }
}
