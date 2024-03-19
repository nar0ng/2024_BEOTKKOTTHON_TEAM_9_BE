package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.post.dto.LikeModel;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;

@Entity
@Table(name = "likes")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@IdClass(LikeEntity.class)
public class LikeEntity extends BaseEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public LikeEntity(LikeModel model) {
    }

    @Override
    public LikeModel toModel() { return new LikeModel(this); }
}
