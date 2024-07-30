package com.example.bommeong.biz.user.controller;

import com.example.bommeong.biz.adopt.dto.AdoptApplicantDto;
import com.example.bommeong.biz.user.dto.AdoptionStatusDto;
import com.example.bommeong.biz.user.dto.BomListDto;
import com.example.bommeong.biz.user.dto.ShelterDtoReq;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.example.bommeong.biz.user.service.ShelterService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/shelter")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Shelter", description = "보호소 API")
@Slf4j
public class ShelterController extends BaseApiController<BaseApiDto<?>> {

    private final String BASE_UPLOAD_DIR = "shelter";
    private final ShelterService shelterService;

    @PostMapping(path = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "보호소 회원가입", description = "보호소 웹 회원가입")
    @ApiResponse(responseCode = "0000", description = "가입 성공", content = @Content(mediaType = "application/json"))
    public ResponseEntity<BaseApiDto<?>> signup(@ModelAttribute ShelterDtoReq.SignUpDto signUpDto) {
        try {

            log.info("data = {}", signUpDto);
            LocalDate now = LocalDate.now();
            String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String dirName = BASE_UPLOAD_DIR + "/" +  signUpDto.getEmail() +  "/" + today;

            shelterService.signUp(signUpDto, dirName);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "보호소 회원가입 실패 : " + e.getMessage());

        }
    }

    @Operation(summary = "보호소 로그인", description = "보호소 웹 로그인")
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "test@naver.com"),
            @Parameter(name = "password", description = "패스워드", example = "1234")
    })
    @PostMapping("/login")
    public ResponseEntity<BaseApiDto<?>> login(@RequestBody UserDtoReq.LoginDto loginDto) {
        try {
            log.info("data = {}", loginDto);
            return super.ok(new BaseApiDto<>(shelterService.login(loginDto)));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "보호소 로그인 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/bom-lists/{shelterId}")
    public ResponseEntity<BaseApiDto<?>> findAllBomLists(@PathVariable Long shelterId) {
        try {
            List<BomListDto> bomList = shelterService.findAllBomListByShelterId(shelterId);

            return super.ok(new BaseApiDto<>(bomList));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "보호견 리스트 조회 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/adopt-lists/{shelterId}")
    public ResponseEntity<BaseApiDto<?>> findAdoptionStatsByShelterId(@PathVariable Long shelterId) {
        try {
            AdoptionStatusDto stats = shelterService.getAdoptionStatsByShelterId(shelterId);
            return super.ok(new BaseApiDto<>(stats));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "입양 통계 조회 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/{postId}/applicants")
    public ResponseEntity<BaseApiDto<?>> findAdoptionApplicationsByPostId(@PathVariable Long postId) {
        try {
            List<AdoptApplicantDto> applications = shelterService.findAdoptionApplicationsByPostId(postId);
            return super.ok(new BaseApiDto<>(applications));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "입양 신청자 조회 실패 : " + e.getMessage());
        }
    }

}
