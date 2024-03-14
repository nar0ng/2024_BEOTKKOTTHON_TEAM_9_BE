package com.example.bommeong.biz.sample;

import com.example.bommeong.biz.sample.dto.SampleBoardEntity;
import com.example.bommeong.biz.sample.dto.SampleBoardModel;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleBoardServiceWithJpa extends BaseServiceImplWithJpa<SampleBoardModel, SampleBoardEntity, Long, SampleBoardRepository> {


    //레포지토리는 BaseService 에 선언된 repository 에 붙여야 한다. lombok 사용 X
    public SampleBoardServiceWithJpa(SampleBoardRepository sampleBoardRepository) {
        this.repository = sampleBoardRepository;
    }




}
