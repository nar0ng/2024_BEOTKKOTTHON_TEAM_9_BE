package com.example.bommeong.biz.adopt.repository;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationStatus;
import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dao.PostStatus;
import com.example.bommeong.biz.user.domain.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdoptRepository extends JpaRepository<AdoptEntity, Long> {

//    Optional<AdoptEntity> findByPostEntity(PostEntity entity);

    Optional<AdoptEntity> findByUser(UserEntity userEntity);

    int countAdoptByDateTime(Long shelterId, LocalDateTime dateTime);
    int countByPostShelterIdAndStatus(Long shelterId, AdoptApplicationStatus status);

    List<AdoptEntity> findByPostPostId(Long postId);

    Optional<AdoptEntity> findByPostPostIdAndUserId(Long postId, Long memberId);

    Optional<AdoptEntity> findByUserId(Long memberId);

    @Query("SELECT COUNT(a) FROM AdoptEntity a WHERE a.post.shelter.id = :shelterId AND a.createdAt >= :startOfDay AND a.createdAt < :endOfDay")
    int countTodayAdoptionRequests(@Param("shelterId") Long shelterId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
