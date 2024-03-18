package com.example.bommeong.biz.chat.repository;

import com.example.bommeong.biz.chat.domain.Chat;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserAndPost(User user, PostEntity post);

}
