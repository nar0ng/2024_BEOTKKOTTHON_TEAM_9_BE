package com.example.bommeong.biz.post.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.post.dao.BomInfoEntity;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService extends BaseServiceImplWithJpa<PostModel, PostEntity, Long, PostRepository> {

    private final AwsS3Service awsS3Service;
    public PostService(PostRepository postRepository, AwsS3Service awsS3Service) {
        this.repository = postRepository;
        this.awsS3Service = awsS3Service;
    }

    public List<PostModel.PostList> findAll() throws Exception {
        return repository.findAll().stream().map(PostModel.PostList::new).collect(Collectors.toList());

    }

    @Transactional
    public void add(PostModel model, String dirName) throws Exception {
        PostEntity postEntity = model.toEntity();
        
        // Post entity 설정
        AwsS3Dto awsS3Dto = awsS3Service.upload(model.getUploadFile(), dirName);
        postEntity.setImageUrl(awsS3Dto.getPath());
        postEntity.setImageName(awsS3Dto.getKey());
//        postEntity.setImageUrl("awsS3Dto.getPath()");
//        postEntity.setImageName("awsS3Dto.getKey()");
        postEntity.setStatus("before");
        
        // BomInfo entity 설정
        BomInfoEntity bomInfoEntity = new BomInfoEntity(model);

        // 매핑
        postEntity.setBomInfoEntity(bomInfoEntity);
        bomInfoEntity.setPostEntity(postEntity);

        repository.save(postEntity);
    }
}
