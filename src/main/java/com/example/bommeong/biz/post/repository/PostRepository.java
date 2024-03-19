package com.example.bommeong.biz.post.repository;

import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.common.dao.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends BaseRepository<PostEntity, Long> {

    Optional<PostEntity> findPostEntityByPostId(Long postId);

    @Query(nativeQuery = true, value = "SELECT *, shelter FROM post p WHERE p.post_id IN (:ids)")
    List<PostEntity> findByLikes(@Param("postIdList") List<Long> ids);

    List<PostEntity> findByPostIdIn(List<Long> ids);

}
