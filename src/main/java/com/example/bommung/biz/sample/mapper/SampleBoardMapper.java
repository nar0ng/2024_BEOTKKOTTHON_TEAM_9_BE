package com.example.bommung.biz.sample.mapper;

import com.example.bommung.common.dao.BaseMapper;
import com.example.bommung.biz.sample.dto.SampleBoardEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoardMapper extends BaseMapper<SampleBoardEntity, Long> {
}
