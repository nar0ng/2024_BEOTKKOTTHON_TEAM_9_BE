package com.example.bommeong.biz.content.repository;

import com.example.bommeong.biz.content.dto.ContentEntity;
import com.example.bommeong.common.dao.BaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends BaseRepository<ContentEntity, Long> {
    List<ContentEntityInterface> findByViewYn(String viewYn, Sort sort);

    List<ContentEntityInterface> findAllByOrderByViewCountDesc();

    List<ContentEntityInterface> findAllByTitleContainingOrDescriptionContaining(String keyword, String keyword2, Sort sort);

    List<ContentEntityInterface> findByUserId(Long userId);

    @Modifying
    @Query("update ContentEntity set viewCount = viewCount + 1 where contentId = :contentId")
    int updateViewCount(@Param(value = "contentId") Long contentId);


}

