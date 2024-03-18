package com.example.bommeong.biz.chat.service;

import com.example.bommeong.biz.chat.domain.Chat;
import com.example.bommeong.biz.chat.repository.ChatRepository;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.biz.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;


    private Chat createNewChat(Chat chat){
        return Chat.builder()
                .user(chat.getUser())
                .input(chat.getInput())
                .response(chat.getResponse())
                .post(chat.getPost())
                .build();
    }


    public void saveChat(Chat chat){
        try{
            User user = chat.getUser();
            PostEntity post = chat.getPost();

            List<Chat> existingChats = chatRepository.findByUserAndPost(user, post);

            if (existingChats.isEmpty()){
                Chat newChat = createNewChat(chat);
                System.out.println("create new chat");
                chatRepository.save(newChat);
            }

        } catch (Exception e) {
            log.error("Error occurred while saving chat", e);
            throw new RuntimeException("Error occurred while saving chat", e);
        }
    }
}
