package com.example.bommung.biz.sample;

import com.example.bommung.common.dao.BaseRepository;
import com.example.bommung.biz.sample.dto.SampleBoardEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoardRepository extends BaseRepository<SampleBoardEntity, Long> {

}
