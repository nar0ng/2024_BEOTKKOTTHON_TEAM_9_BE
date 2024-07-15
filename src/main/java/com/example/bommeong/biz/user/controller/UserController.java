package com.example.bommeong.biz.user.controller;

import com.example.bommeong.biz.user.dto.TokenDto;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.example.bommeong.biz.user.dto.UserDtoRes;
import com.example.bommeong.biz.user.service.TokenService;
import com.example.bommeong.biz.user.service.UserService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import com.example.bommeong.common.utils.ResponseEntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 API")
public class UserController extends BaseApiController<BaseApiDto<?>> {

    private final TokenService tokenService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "반려인, 보호소 통합 회원가입")
    @ApiResponse(responseCode = "0000", description = "가입 성공", content = @Content(mediaType = "application/json"))
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "test@naver.com"),
            @Parameter(name = "password", description = "패스워드", example = "1234"),
            @Parameter(name = "name", description = "표기 이름", example = "@@보호소"),
            @Parameter(name = "phone", description = "연락처", example = "01012345678"),
            @Parameter(name = "memberType", description = "S: 보호소, B: 반려인, A: 어드민", example = "B")
    })
    public ResponseEntity<BaseApiDto<?>> signup(@RequestBody UserDtoReq.SignUpDto signUpDto) throws Exception {
        try {
            userService.signUp(signUpDto);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
//            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "회원가입 실패 : " + e.getMessage());
        }

    }

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "통합 로그인")
    @ApiResponse(responseCode = "0000", description = "로그인 성공", content = @Content(mediaType = "application/json"))
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "test@naver.com"),
            @Parameter(name = "password", description = "패스워드", example = "1234")
    })
    public ResponseEntity<BaseApiDto<?>> login(@RequestBody UserDtoReq.LoginDto loginDto) throws Exception {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            UserDtoRes.TokenDto token = userService.login(loginDto);
            String accesToken = token.getAccess_token().toString();
            String refreshToken = token.getRefresh_token().toString();

            //httpHeaders.add("Authorization", "Bearer " + accesToken);
            httpHeaders.add("accessAuthorization", "Bearer " + accesToken);
            httpHeaders.add("refreshAuthorization", "Bearer " + refreshToken);
            return super.ok(new BaseApiDto<>(token));
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "로그인 실패 : " + e.getMessage());
        }
    }

    @Operation(summary = "토큰 리프레시", description = "리프레시 토큰으로 억세스 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<BaseApiDto<?>> refresh(@RequestBody TokenDto token) throws Exception {
    try {
        TokenDto reissue = tokenService.reissue(token);
        return super.ok(new BaseApiDto<>(reissue));
    } catch (Exception e) {
        return super.fail(BaseApiDto.newBaseApiDto(), "9999", "토큰 리프레시 실패 : " + e.getMessage());
    }

        //        return new ResponseEntity<>(userService.refreshAccessToken(token), HttpStatus.OK);

    }

    @Operation(summary = "유저 정보 수정", description = "")
    @PutMapping()
    public ResponseEntity<BaseApiDto<?>> updateUser(@RequestBody UserDtoReq.UpdateDto updateDto) throws Exception {
        try {
            userService.updateUser(updateDto);
            return ResponseEntityUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "유저 업데이트 실패 : " + e.getMessage());
        }

    }

    @Operation(summary = "유저 삭제", description = "반려인, 보호소 통합 회원가입")
    @DeleteMapping()
    public ResponseEntity<BaseApiDto<?>> deleteUser() throws Exception {
        try {
            userService.deleteUser();
            return ResponseEntityUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "유저 삭제 실패 : " + e.getMessage());
        }
    }

    @GetMapping()
    @Operation(summary = "유저 정보", description = "토큰값으로 유저 정보 가져오기")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoRes.UserRes getUser() throws Exception {
        return userService.getUser();
    }

    @GetMapping("/info/{memberId}")
    @Operation(summary = "마이페이지 정보", description = "앱 내 마이페이지 로딩에 필요한 유저 인포 & 입양 신청 정보")
    public ResponseEntity<BaseApiDto<?>> myPageData(@PathVariable Long memberId) throws Exception {
        try {
            UserDtoRes.MyPageDto myPageDto = userService.getMyPage(memberId);
            return super.ok(new BaseApiDto<>(myPageDto));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "마이페이지 조회 실패 : " + e.getMessage());
        }
    }


}

