package com.example.bommeong.web;


import com.example.bommeong.common.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/cms")
public class IndexController extends BaseController {


    @GetMapping(path = {"/", ""})
    public String root() {
        log.debug("# IN IndexController.root()");
        return "redirect:/cms/index";
    }

    @GetMapping("/index")
    public String index() {
        return "cms/index";
    }

    @GetMapping("/error")
    public String error() {
        return "cms/error";
    }

    @GetMapping("/login")
    public String login() {
        return "cms/login";
    }

//    @PostMapping("login-process")
//    public String loginProcess(UserInfo userInfo) {
//        log.info("# login Process : userInfo = {}", userInfo);
//        return "index";
//    }
//
//    @GetMapping("login-error")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        return "loginForm";
//    }

}
