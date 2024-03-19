package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.post.dto.LikeModel;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Long memberId;
    private Long postId;

    public LikeId(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }
}
