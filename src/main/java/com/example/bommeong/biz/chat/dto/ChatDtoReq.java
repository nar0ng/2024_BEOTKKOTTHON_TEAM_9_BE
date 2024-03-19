package com.example.bommeong.biz.chat.dto;

import com.example.bommeong.biz.post.dto.BomInfoModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDtoReq {
    private String input;
    private BomInfoModel bomInfo;
}
