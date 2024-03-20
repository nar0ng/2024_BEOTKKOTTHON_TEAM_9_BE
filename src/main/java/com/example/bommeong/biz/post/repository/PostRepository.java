package com.example.bommeong.biz.post.repository;

import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends BaseRepository<PostEntity, Long> {
    Optional<PostEntity> findPostEntityByPostId(Long postId);
}
