package com.example.bommeong.biz.post.controller;

import com.example.bommeong.biz.content.dto.ContentModel;
import com.example.bommeong.biz.post.dto.LikeModel;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.post.service.PostService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import com.example.bommeong.common.exception.BizException;
import com.example.bommeong.common.utils.ResponseEntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "공고 API")
public class PostController extends BaseApiController<BaseApiDto<?>> {

    private final PostService postService;

    private final String BASE_UPLOAD_DIR = "post";
    @GetMapping
    @Operation(summary = "공고 리스트", description = "공고 + bomInfo 리스트 조회")
    public ResponseEntity<BaseApiDto<?>> findAll() throws Exception {
        try {
            List<PostModel.PostList> list = postService.findAll();
            return super.ok(new BaseApiDto<>(list));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "공고 리스트 조회 실패 : " + e.getMessage());
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "공고 등록하기", description = "보호소로 로그인한 사용자가 등록할 수 있다.")
    @Parameters({
            @Parameter(name = "shelterId", description = "보호소 memberId", example = "1"),
            @Parameter(name = "uploadFile", description = "업로드 할 이미지 파일", example = "dog.png"),
            @Parameter(name = "bomInfo.name", description = "강아지 이름", example = "날봄이"),
            @Parameter(name = "bomInfo.age", description = "나이", example = "3"),
            @Parameter(name = "bomInfo.gender", description = "성별", example = "여"),
            @Parameter(name = "bomInfo.breed", description = "견종", example = "골든리트리버"),
            @Parameter(name = "bomInfo.personality", description = "성격", example = "귀엽고 애교많음"),
            @Parameter(name = "bomInfo.extra", description = "추가 정보", example = "산책을 좋아하고 청소기 소리를 싫어해요"),
    })
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
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "공고 작성 실패 : " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "공고 삭제", description = "공고 id 값으로 해당 공고 삭제")
    public ResponseEntity<BaseApiDto<?>> remove(@PathVariable Long postId) {
        try {
             postService.remove(postId);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "공고 삭제 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/like")
    public ResponseEntity<BaseApiDto<?>> setLike(@RequestBody LikeModel likeModel) throws Exception {
        try {
            String flag = likeModel.getFlag();
            switch (flag) {
                case "register" -> postService.likePost(likeModel);
                case "remove" -> postService.unLikePost(likeModel);
                default -> throw new RuntimeException("no flag");
            }
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "좋아요 등록/삭제 실패 : " + e.getMessage());
        }
    }
}
