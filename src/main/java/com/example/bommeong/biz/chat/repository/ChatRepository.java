package com.example.bommeong.biz.chat.repository;

import com.example.bommeong.biz.chat.domain.Chat;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserAndPost(Optional<UserEntity> user, Optional<PostEntity> post);
    @Query("SELECT DISTINCT c.post FROM chat c WHERE c.user = :user")
    List<PostEntity> findPostsByUser(@Param("user") UserEntity user);

}
