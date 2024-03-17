package com.example.bommeong.biz.post.controller;

import com.example.bommeong.biz.content.dto.ContentModel;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.post.service.PostService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import com.example.bommeong.common.exception.BizException;
import com.example.bommeong.common.utils.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/post")
public class PostController extends BaseApiController<BaseApiDto<?>> {

    private final PostService postService;

    private final String BASE_UPLOAD_DIR = "post";
    @GetMapping(path = {"", "/"})
    public ResponseEntity<BaseApiDto<?>> findAll() throws Exception {
        try {
            List<PostModel.PostList> list = postService.findAll();
            return super.ok(new BaseApiDto<>(list));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "조회 실패 : " + e.getMessage());
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseApiDto<?>> add(@ModelAttribute PostModel model) {
        try {
            log.info("data = {}", model);
            LocalDate now = LocalDate.now();
            String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String dirName = BASE_UPLOAD_DIR + "/" +  model.getShelterId() +  "/" + today;
            postService.add(model, dirName);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "공고작성 실패 : " + e.getMessage());
        }
    }
}
