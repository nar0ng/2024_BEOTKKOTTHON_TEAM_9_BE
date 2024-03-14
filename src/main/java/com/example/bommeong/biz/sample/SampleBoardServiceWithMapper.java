package com.example.bommeong.biz.sample;

import com.example.bommeong.common.code.ResultCode;
import com.example.bommeong.common.exception.BizException;
import com.example.bommeong.common.service.BaseServiceImplWithMapper;
import com.example.bommeong.biz.sample.dto.SampleBoardEntity;
import com.example.bommeong.biz.sample.dto.SampleBoardModel;
import com.example.bommeong.biz.sample.mapper.SampleBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleBoardServiceWithMapper
        extends BaseServiceImplWithMapper<SampleBoardModel, SampleBoardEntity, Long, SampleBoardMapper> {
    //일단 JPA로 되지 않을까 싶음
    public SampleBoardServiceWithMapper(SampleBoardMapper sampleBoardMapper) {
        this.mapper = sampleBoardMapper;
    }


    @Override
    public void remove(Long pk) throws Exception {
        throw new BizException(ResultCode.NOT_SUPPORTED);
    }
}
