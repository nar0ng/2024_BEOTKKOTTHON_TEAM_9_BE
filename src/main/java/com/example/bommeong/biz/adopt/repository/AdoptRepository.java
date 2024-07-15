package com.example.bommeong.biz.adopt.repository;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdoptRepository extends JpaRepository<AdoptEntity, Long> {

//    Optional<AdoptEntity> findByPostEntity(PostEntity entity);

    Optional<AdoptEntity> findByUser(UserEntity userEntity);
}
