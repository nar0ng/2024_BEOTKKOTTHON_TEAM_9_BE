package com.example.bommung.biz.content.repository;

import com.example.bommung.biz.content.dto.TagEntity;
import com.example.bommung.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends BaseRepository<TagEntity, Long> {
}
