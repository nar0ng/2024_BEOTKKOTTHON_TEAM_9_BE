package com.example.bommeong.biz.adopt.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.adopt.repository.AdoptRepository;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AdoptService {
    private final AwsS3Service awsS3Service;
    private final AdoptRepository adoptRepository;
    private final PostRepository postRepository;

    @Transactional
    public void add(AdoptModel model, String dirName) throws Exception {
        AdoptEntity adoptEntity = model.toEntity();


        Optional<PostEntity> entity = postRepository.findById(model.getPostId());
        // post 존재 확인
        if (entity.isEmpty()) throw new RuntimeException("공고가 없습니다.");

        // adopt 확인
        if (adoptRepository.findByPostEntity(entity.get()).isPresent()) throw new RuntimeException("이미 입양신청 된 공고입니다.");;

        // S3 업로드 후 Post entity 설정
        AwsS3Dto awsS3Dto = awsS3Service.upload(model.getUploadFile(), dirName);
        adoptEntity.setImageUrl(awsS3Dto.getPath());
        adoptEntity.setImageName(awsS3Dto.getKey());
        adoptEntity.setStatus("A");

        // BomInfo entity 설정
        AdoptApplicationEntity adoptApplicationEntity = new AdoptApplicationEntity(model);

        // 매핑
        adoptEntity.setAdoptApplicationEntity(adoptApplicationEntity);
        adoptApplicationEntity.setAdoptEntity(adoptEntity);

        adoptRepository.save(adoptEntity);
    }
}
