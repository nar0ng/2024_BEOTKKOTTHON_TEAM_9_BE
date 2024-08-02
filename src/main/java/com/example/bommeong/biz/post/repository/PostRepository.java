package com.example.bommeong.biz.post.repository;

import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dao.PostStatus;
import com.example.bommeong.common.dao.BaseRepository;
import java.util.Collection;
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

    @Query(nativeQuery = true, value = "SELECT * " +
            ", (SELECT s.name FROM shelter s WHERE s.shelter_id = p.shelter_id) as shelterName " +
            " FROM post p " +
            "WHERE p.shelter_id IN (:shelterIds)")
    List<PostEntity> findAllByShelterIds(@Param("shelterIds") List<Long> shelterIds);

    List<PostEntity> findAllByShelterId(Long shelterId);

    int countByShelterId(Long shelterId);

    int countByShelterIdAndStatus(Long ShelterId, PostStatus status);

    Optional<PostEntity> findByStatus(PostStatus status);
}
