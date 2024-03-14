package com.example.bommeong.biz.user.controller;

import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.example.bommeong.biz.user.dto.UserDtoRes;
import com.example.bommeong.biz.user.service.UserService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import com.example.bommeong.common.utils.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController extends BaseApiController<BaseApiDto<?>> {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseApiDto<?>> signup(@RequestBody UserDtoReq.SignUpDto signUpDto) throws Exception {
        try {
            userService.signUp(signUpDto);
            return ResponseEntityUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "회원가입 실패 : " + e.getMessage());
        }

    }

    @PostMapping("/login")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseApiDto<?>> login(@RequestBody UserDtoReq.LoginDto loginDto) throws Exception {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            UserDtoRes.TokenDto token = userService.login(loginDto);
            String accesToken = token.getAccess_token().toString();
            String refreshToken = token.getRefresh_token().toString();

            //httpHeaders.add("Authorization", "Bearer " + accesToken);
            httpHeaders.add("accssAuthorization", "Bearer " + accesToken);
            httpHeaders.add("refreshAuthorization", "Bearer " + refreshToken);
            return super.ok(new BaseApiDto<>(token));
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "로그인 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<UserDtoRes.TokenDto> refresh(@RequestBody UserDtoRes.TokenDto token) throws Exception {
        return new ResponseEntity<>(userService.refreshAccessToken(token), HttpStatus.OK);
    }

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
    @ResponseStatus(HttpStatus.OK)
    public UserDtoRes.UserRes getUser() throws Exception {
        return userService.getUser();
    }

}

