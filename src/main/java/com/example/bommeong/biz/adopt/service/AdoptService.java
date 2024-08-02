package com.example.bommeong.biz.adopt.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import com.example.bommeong.biz.adopt.dao.AdoptApplicationStatus;
import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.dto.AdoptApplicationStatusDto;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.adopt.repository.AdoptRepository;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.biz.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AdoptService {
    private final AwsS3Service awsS3Service;
    private final AdoptRepository adoptRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void add(AdoptModel model, String dirName) throws Exception {
        AdoptEntity adoptEntity = model.toEntity();

        // 유저 존재 확인
        Optional<UserEntity> user = userRepository.findById(model.getMemberId());
        if (user.isEmpty()) throw new RuntimeException("user not found");

        // post 존재 확인
        Optional<PostEntity> entity = postRepository.findById(model.getPostId());
        if (entity.isEmpty()) throw new RuntimeException("공고가 없습니다.");

        // 유저의 입양신청 확인
        if (adoptRepository.findByUser(user.get()).isPresent()) throw new RuntimeException("입양 신청 내역이 있습니다.");

        // S3 업로드 후 Post entity 설정
        AwsS3Dto awsS3Dto = awsS3Service.upload(model.getUploadFile(), dirName);
        adoptEntity.setImageUrl(awsS3Dto.getPath());
        adoptEntity.setImageName(awsS3Dto.getKey());
        adoptEntity.setStatus(AdoptApplicationStatus.PENDING);

        // BomInfo entity 설정
        AdoptApplicationEntity adoptApplicationEntity = new AdoptApplicationEntity(model);

        // 매핑
        adoptEntity.setAdoptApplicationEntity(adoptApplicationEntity);
        adoptApplicationEntity.setAdoptEntity(adoptEntity);

        adoptRepository.save(adoptEntity);
    }

    public void updateAdoptApplicationStatus(AdoptApplicationStatusDto statusDto){
        AdoptEntity adoptEntity = adoptRepository.findByUserId(statusDto.memberId())
                .orElseThrow(() -> new RuntimeException("입양 신청을 찾을 수 없습니다."));

        adoptEntity.setStatus(statusDto.adoptApplicationStatus());
        adoptRepository.save(adoptEntity);

    }

    public void deleteAdopt(Long adoptId) {
        AdoptEntity adoptEntity = adoptRepository.findById(adoptId)
                .orElseThrow(() -> new RuntimeException("입양 신청을 찾을 수 없습니다."));

        adoptRepository.delete(adoptEntity);
    }

    public List<AdoptModel> getAdoptList() {
        return adoptRepository.findAll().stream().map(AdoptEntity::toModel).toList();
    }
}
