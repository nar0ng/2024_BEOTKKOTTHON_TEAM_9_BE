package com.example.bommeong.biz.post.repository;

import com.example.bommeong.biz.post.dao.BomInfoEntity;
import com.example.bommeong.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomInfoRepository extends BaseRepository<BomInfoEntity, Long> {
}
