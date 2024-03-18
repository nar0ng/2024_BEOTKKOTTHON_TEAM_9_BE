package com.example.bommeong.biz.chat.controller;

import com.example.bommeong.biz.chat.domain.Chat;
import com.example.bommeong.biz.chat.dto.ChatDtoReq;
import com.example.bommeong.biz.chat.dto.ChatDtoRes;
import com.example.bommeong.biz.chat.service.ChatService;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dto.BomInfoModel;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.biz.user.repository.UserRepository;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/chat")
public class ChatController  extends BaseApiController<BaseApiDto<?>> {
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @PostMapping("/{postId}/{userId}")
    ResponseEntity<BaseApiDto<?>> Chat(@RequestBody ChatDtoReq chatDtoReq,
                                    @PathVariable Long postId,
                                    @PathVariable Long userId ){

        Optional<User> user = userRepository.findUserById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<PostEntity> postEntityOptional = postRepository.findPostEntityByPostId(postId);
        if (postEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PostEntity postEntity = postEntityOptional.get();


        BomInfoModel bomInfo = postEntity.toModel().getBomInfo();
        chatDtoReq.setBomInfo(bomInfo);

        String flaskUrl = "http://127.0.0.1:5001/api/chat";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatDtoReq> entity = new HttpEntity<>(chatDtoReq, headers);

        try{
            ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().equals(HttpStatus.OK)){
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                String input = jsonNode.get("input").asText();
                String chatResponse = jsonNode.get("response").asText();

                Chat chat = Chat.builder()
                        .input(input)
                        .response(chatResponse)
                        .user(user.get())
                        .post(postEntityOptional.get())
                        .build();

                chatService.saveChat(chat);
                ChatDtoRes chatDtoRes = new ChatDtoRes(chat.getResponse());
                return super.ok(new BaseApiDto<>(chatDtoRes));
            }
        } catch (JsonMappingException e) {
            log.error("Error processing JSON response from Flask server", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (JsonProcessingException e) {
            log.error("Error communicating with Flask server", e);
            throw new RuntimeException(e);
        }
        //


        return null;
    }

}
