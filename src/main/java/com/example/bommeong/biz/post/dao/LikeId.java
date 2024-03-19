package com.example.bommeong.biz.post.dao;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public LikeId(Long memberId) {
        this.memberId = memberId;
    }
}
