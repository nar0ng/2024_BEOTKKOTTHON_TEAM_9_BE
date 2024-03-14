package com.example.bommeong.common.controller;

import com.example.bommeong.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;


@Slf4j
//@Controller
@RequiredArgsConstructor
public abstract class BaseController {

    /* 모든 Mapping 메소드의 Model 객체에 공통으로 전달*/
    @ModelAttribute("userInfo")
    public UserInfo setUserInfo(Principal principal) {
        UserInfo userInfo = UserInfo.getInstance(principal);
        log.debug("# BaseController.setUserInfo : userInfo = {}", userInfo);
        return userInfo;
    }
}
