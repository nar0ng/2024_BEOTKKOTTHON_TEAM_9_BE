package com.example.bommeong.biz.post.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.post.dao.BomInfoEntity;
import com.example.bommeong.biz.post.dao.LikeEntity;
import com.example.bommeong.biz.post.dao.LikeId;
import com.example.bommeong.biz.post.dao.PostStatus;
import com.example.bommeong.biz.post.dto.LikeModel;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.repository.LikeRepository;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.user.domain.ShelterEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.biz.user.repository.ShelterRepository;
import com.example.bommeong.biz.user.repository.UserRepository;
import com.example.bommeong.common.code.ResultCode;
import com.example.bommeong.common.dto.PageEntity;
import com.example.bommeong.common.exception.BizException;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PostService extends BaseServiceImplWithJpa<PostModel, PostEntity, Long, PostRepository> {
    private final AwsS3Service awsS3Service;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    public PostService(PostRepository postRepository, AwsS3Service awsS3Service, LikeRepository likeRepository, UserRepository userRepository, ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
        this.repository = postRepository;
        this.awsS3Service = awsS3Service;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public List<PostModel.PostList> findAll() throws Exception {
        return repository.findAll().stream().map(PostModel.PostList::new).collect(Collectors.toList());

    }

    public List<PostModel.PostList> findPostsWithinDistance(Double latitude, Double longitude, Double maxDistance) throws Exception {
        List<ShelterEntity> allWithLocation = shelterRepository.findAllWithLocation(latitude, longitude, maxDistance);
        if (allWithLocation.isEmpty()) return new ArrayList<>();
        List<Long> shelterIds = allWithLocation.stream().map(ShelterEntity::getId).toList();
        List<PostEntity> postEntities = repository.findAllByShelterIds(shelterIds);
        return postEntities.stream().map(PostModel.PostList::new).collect(Collectors.toList());
    }

    public PostModel.PostList findDetail(Long postId) throws Exception {
        Optional<PostEntity> detail = repository.findById(postId);
        if (detail.isEmpty()) throw  new RuntimeException("조회 데이터 없음");

        return new PostModel.PostList(detail.get());
    }

    public List<PostModel.PostList> findLikeList(Long memberId) throws Exception {
        // memberId 로 검색할 likeId 객체 생성
        LikeId likeId = new LikeId(memberId);
        // likes 테이블에서 memberId 값으로 검색
//        Optional<List<LikeEntity>> list = likeRepository.findByLikeId(likeId);
        Optional<List<LikeEntity>> list = likeRepository.findAllPostIdByMemberId(memberId);
        if (list.isEmpty()) return new ArrayList<>();
        // 검색한 likeList 에서 postId 리스트로 뽑아내기
//        List<PostEntity> postEntityList= list.get().stream().map(PostEntity::new).toList();
        List<Long> postEntityList = list.get().stream().map(entity -> entity.getPost().getPostId()).toList();

        return repository.findByPostIdIn(postEntityList).stream().map(PostModel.PostList::new).collect(Collectors.toList());
        
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
        postEntity.setStatus(PostStatus.BEFORE);
        
        // BomInfo entity 설정
        BomInfoEntity bomInfoEntity = new BomInfoEntity(model);

        // 매핑
        postEntity.setBomInfoEntity(bomInfoEntity);
        bomInfoEntity.setPost(postEntity);

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
    public void likePost(LikeModel likeModel) throws Exception {
        UserEntity userEntity = userRepository.findById(likeModel.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));
        PostEntity post = repository.findById(likeModel.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));


        LikeEntity entity = new LikeEntity(userEntity, post);
//        Optional<LikeEntity> likeEntity = likeRepository.findById(entity);
//        if (likeEntity.isPresent()) System.out.println("있음");
//        if (likeEntity.isEmpty()) System.out.println("없음");

        likeRepository.save(entity);
    }

    @Transactional
    public void unLikePost(LikeModel likeModel) throws Exception {
        UserEntity userEntity = userRepository.findById(likeModel.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));
        PostEntity post = repository.findById(likeModel.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));

        LikeEntity entity = new LikeEntity(userEntity, post);

//        Optional<LikeEntity> like = likeRepository.findByUserAndPost(user, post);
//        System.out.println("---- 좋아요 제거 ----");
//
//        // 좋아요 존재하는지 체크
//        if (like.isEmpty()) {
//            System.out.println(" --- 좋아요 테이블에 없음;;");
//            return;
//        }
//
        likeRepository.delete(entity);

    }

    public PageEntity<PostModel> getListForAdmin(PageEntity<PostModel> pageEntity) throws Exception {
        Page<PostEntity> page = repository.findAll(toPageable(pageEntity));
        Stream<PostModel> stream = page.getContent().stream().map(PostEntity::toModel);

        pageEntity.setTotalCnt(page.getTotalElements());
        pageEntity.setDtoList(stream.toList());
        return pageEntity;
    }

}
