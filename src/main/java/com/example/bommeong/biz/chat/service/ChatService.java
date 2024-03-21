package com.example.bommeong.biz.chat.service;

import com.example.bommeong.biz.chat.domain.Chat;
import com.example.bommeong.biz.chat.dto.ChatPostListDtoRes;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


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
                Chat newChat = createNewChat(chat);
                System.out.println("create new chat");
                chatRepository.save(newChat);
        } catch (Exception e) {
            log.error("Error occurred while saving chat", e);
            throw new RuntimeException("Error occurred while saving chat", e);
        }
    }

    public List<Chat> getChatRecords(Long userId, Long postId){

        Optional<User> user = userRepository.findUserById(userId);
        if (user.isEmpty()) {
            return null;
        }

        Optional<PostEntity> postEntityOptional = postRepository.findPostEntityByPostId(postId);
        if (postEntityOptional.isEmpty()) {
            return null;
        }

        List<Chat> chatRecords = chatRepository.findByUserAndPost(user, postEntityOptional);
        if (!chatRecords.isEmpty()) {
            return chatRecords;
        } else {
            return null;
        }
    }

    public List<ChatPostListDtoRes> getPostsByUserIds(Long userId) {
        Optional<User> userOptional = userRepository.findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        List<PostEntity> posts = chatRepository.findPostsByUser(user);

        if (!posts.isEmpty()){
            return posts.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }
        else {
            return null;
        }


    }

    private ChatPostListDtoRes mapToDto(PostEntity postEntity) {
        return ChatPostListDtoRes.builder()
                .postId(postEntity.getPostId())
                .name(postEntity.getBomInfoEntity().getName())
                .imageName(postEntity.getImageName())
                .imageUrl(postEntity.getImageUrl())
                .status(postEntity.getStatus())
                .createdAt(postEntity.getCreatedAt())
                .build();
    }


}
