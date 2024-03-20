package com.example.bommeong.biz.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatPostListDtoRes {
    private Long postId;
    private String name;
    private String imageName;
    private String imageUrl;
    private String status;
}
