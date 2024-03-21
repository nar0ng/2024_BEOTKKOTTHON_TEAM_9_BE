package com.example.bommeong.biz.adopt.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.adopt.repository.AdoptRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AdoptService {
    private final AwsS3Service awsS3Service;
    private final AdoptRepository adoptRepository;

    @Transactional
    public void add(AdoptModel model, String dirName) throws Exception {
        AdoptEntity adoptEntity = model.toEntity();

        // S3 업로드 후 Post entity 설정
        AwsS3Dto awsS3Dto = awsS3Service.upload(model.getUploadFile(), dirName);
        adoptEntity.setImageUrl(awsS3Dto.getPath());
        adoptEntity.setImageName(awsS3Dto.getKey());
        adoptEntity.setStatus("before");

        // BomInfo entity 설정
        AdoptApplicationEntity adoptApplicationEntity = new AdoptApplicationEntity(model);

        // 매핑
        adoptEntity.setAdoptApplicationEntity(adoptApplicationEntity);
        adoptApplicationEntity.setAdoptApplicationEntity(adoptApplicationEntity);

        adoptRepository.save(adoptEntity);
    }
}
