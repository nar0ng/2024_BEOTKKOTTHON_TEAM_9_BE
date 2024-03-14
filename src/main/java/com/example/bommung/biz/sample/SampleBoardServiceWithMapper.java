package com.example.bommung.biz.sample;

import com.example.bommung.common.code.ResultCode;
import com.example.bommung.common.exception.BizException;
import com.example.bommung.common.service.BaseServiceImplWithMapper;
import com.example.bommung.biz.sample.dto.SampleBoardEntity;
import com.example.bommung.biz.sample.dto.SampleBoardModel;
import com.example.bommung.biz.sample.mapper.SampleBoardMapper;
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
