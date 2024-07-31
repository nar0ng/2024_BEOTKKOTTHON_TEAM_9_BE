package com.example.bommeong.biz.adopt.repository;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdoptRepository extends JpaRepository<AdoptEntity, Long> {

//    Optional<AdoptEntity> findByPostEntity(PostEntity entity);

    Optional<AdoptEntity> findByUser(UserEntity userEntity);

    int countByPostShelterIdAndCreatedAtAfter(Long shelterId, LocalDateTime dateTime);
    int countByPostShelterIdAndStatus(Long shelterId, String status);

    List<AdoptEntity> findByPostPostId(Long postId);

    Optional<AdoptEntity> findByPostPostIdAndUserId(Long postId, Long memberId);

}
