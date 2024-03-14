package com.example.bommeong.biz.content.repository;

import com.example.bommeong.biz.content.dto.TagEntity;
import com.example.bommeong.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends BaseRepository<TagEntity, Long> {
}
