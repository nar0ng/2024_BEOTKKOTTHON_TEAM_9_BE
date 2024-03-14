package com.example.bommeong.biz.sample;

import com.example.bommeong.common.dao.BaseRepository;
import com.example.bommeong.biz.sample.dto.SampleBoardEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoardRepository extends BaseRepository<SampleBoardEntity, Long> {

}
