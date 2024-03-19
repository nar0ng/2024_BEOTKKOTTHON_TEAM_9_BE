package com.example.bommeong.biz.post.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.post.dao.BomInfoEntity;
import com.example.bommeong.biz.post.dao.LikeEntity;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.repository.LikeRepository;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.biz.user.repository.UserRepository;
import com.example.bommeong.common.code.ResultCode;
import com.example.bommeong.common.exception.BizException;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService extends BaseServiceImplWithJpa<PostModel, PostEntity, Long, PostRepository> {

    private final AwsS3Service awsS3Service;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    public PostService(PostRepository postRepository, AwsS3Service awsS3Service, LikeRepository likeRepository, UserRepository userRepository) {
        this.repository = postRepository;
        this.awsS3Service = awsS3Service;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public List<PostModel.PostList> findAll() throws Exception {
        return repository.findAll().stream().map(PostModel.PostList::new).collect(Collectors.toList());

    }

    @Transactional
    public void add(PostModel model, String dirName) throws Exception {
        PostEntity postEntity = model.toEntity();
        
        // S3 업로드 후 Post entity 설정
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

    @Override
    @Transactional
    public void remove(Long pk) throws Exception {
        PostEntity postEntity = repository.findById(pk)
                .orElseThrow(() -> new BizException(ResultCode.DATA_NOT_FOUND));
        // S3 오브젝트 삭제
        AwsS3Dto awsS3Dto = new AwsS3Dto(postEntity.getImageName(), "");
        awsS3Service.remove(awsS3Dto);
        super.remove(pk);
    }

    @Transactional
    public void likePost(Long memberId, Long postId) throws Exception {
        User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        PostEntity post = repository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        LikeEntity like = new LikeEntity();
        like.setUser(user);
        like.setPost(post);

        // 좋아요 존재하는지 체크
        if (likeRepository.existsAllByUserIsAndPost(user, post)) return;

        likeRepository.save(like);
    }

    @Transactional
    public void unLikePost(Long memberId, Long postId) throws Exception {
        User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        PostEntity post = repository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<LikeEntity> like = likeRepository.findByUserAndPost(user, post);
        System.out.println("---- 좋아요 제거 ----");
        // 좋아요 존재하는지 체크
        if (like.isEmpty()) {
            System.out.println(" --- 좋아요 테이블에 없음;;");
            return;
        }

        likeRepository.delete(like.get());
    }
}
