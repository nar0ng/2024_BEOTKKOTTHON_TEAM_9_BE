package com.example.bommeong.web;


import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.post.service.PostService;
import com.example.bommeong.biz.user.dto.UserDtoRes;
import com.example.bommeong.biz.user.service.UserService;
import com.example.bommeong.common.controller.BaseController;
import com.example.bommeong.common.dto.PageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/cms")
@RequiredArgsConstructor
public class IndexController extends BaseController {

    private final PostService postService;
    private final UserService userService;

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

    @GetMapping("/user")
    public String user(Pageable pageable, Model model) throws Exception {
        PageEntity<UserDtoRes.UserListAdmin> pageEntity = userService.getAllUserForAdmin(new PageEntity<>(pageable));
        model.addAttribute("contentList", pageEntity);
        return "cms/user/index";
    }

    @GetMapping("/content")
    public String post(Pageable pageable, Model model) throws Exception {
        PageEntity<PostModel> pageEntity = postService.getListForAdmin(new PageEntity<>(pageable));
        model.addAttribute("contentList", pageEntity);
        return "cms/content/index";
    }

    @GetMapping("/content/{contentId}")
    public String detail(@PathVariable("contentId") Long id, Model model) throws Exception {
        PostModel.PostList contentModel = postService.findDetail(id);
        log.info("# content detail page = {}", contentModel);
        model.addAttribute("contentDetail", contentModel);
        return "cms/content/detail";
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
