package com.example.bommeong.biz.sample.mapper;

import com.example.bommeong.common.dao.BaseMapper;
import com.example.bommeong.biz.sample.dto.SampleBoardEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBoardMapper extends BaseMapper<SampleBoardEntity, Long> {
}
