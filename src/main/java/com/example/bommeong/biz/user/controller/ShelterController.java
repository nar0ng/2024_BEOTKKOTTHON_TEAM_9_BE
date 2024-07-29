package com.example.bommeong.biz.user.controller;

import com.example.bommeong.biz.user.dto.ShelterDtoReq;
import com.example.bommeong.biz.user.service.ShelterService;
import com.example.bommeong.biz.user.service.UserService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shelter")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Shelter", description = "보호소 API")
public class ShelterController extends BaseApiController<BaseApiDto<?>> {

    private final ShelterService shelterService;

    @PostMapping("/signup")
    @Operation(summary = "보호소 회원가입", description = "보호소 웹 회원가입")
    @ApiResponse(responseCode = "0000", description = "가입 성공", content = @Content(mediaType = "application/json"))
    public ResponseEntity<BaseApiDto<?>> signup(@RequestBody ShelterDtoReq.SignUpDto signUpDto) throws Exception {
        try {
            shelterService.signUp(signUpDto);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "보호소 회원가입 실패 : " + e.getMessage());

        }
    }
}
