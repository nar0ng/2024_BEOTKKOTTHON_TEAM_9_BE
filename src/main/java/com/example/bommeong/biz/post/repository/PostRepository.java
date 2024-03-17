package com.example.bommeong.biz.post.repository;

import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends BaseRepository<PostEntity, Long> {
}
