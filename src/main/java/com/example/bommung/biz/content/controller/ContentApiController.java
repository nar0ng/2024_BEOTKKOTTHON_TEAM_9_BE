package com.example.bommung.biz.content.controller;

import com.example.bommung.biz.content.service.ContentService;
import com.example.bommung.common.controller.BaseApiController;
import com.example.bommung.common.controller.BaseApiDto;
import com.example.bommung.common.utils.ResponseEntityUtil;
import com.example.bommung.biz.content.dto.ContentModel;
import com.example.bommung.biz.content.dto.HistoryModel;
import jakarta.servlet.http.HttpServletRequest;
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
@CrossOrigin("*")
@RequestMapping("/api/v1/content")
public class ContentApiController extends BaseApiController<BaseApiDto<?>> {
    private final ContentService contentService;
    private final String BASE_UPLOAD_DIR = "content";

    @GetMapping(path = {"", "/"})
    public ResponseEntity<BaseApiDto<?>> findAll() {
        try {
            List<ContentModel> list = contentService.getListByViewYn(10);
            return super.ok(new BaseApiDto<>(list));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "조회 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/realtime")
    public ResponseEntity<BaseApiDto<?>> findRealTime() throws Exception {
        try {
            List<HistoryModel> list = contentService.getListByViewCount(10);
            return super.ok(new BaseApiDto<>(list));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "조회 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<BaseApiDto<?>> search(@RequestParam(name = "keyword") String keyword) throws Exception {
        try {
            List<ContentModel> list = contentService.search(keyword);
            return super.ok(new BaseApiDto<>(list));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "조회 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<BaseApiDto<?>> findUserContent(@RequestParam(name = "userId") Long userId) throws Exception {
        try {
            List<ContentModel> list = contentService.getListByUserId(userId);
            return super.ok(new BaseApiDto<>(list));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "조회 실패 : " + e.getMessage());
        }
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<BaseApiDto<?>> detail(@PathVariable("contentId") Long id, HttpServletRequest request) throws Exception {
        log.info("# api request ={}", request);
        try {
            ContentModel contentModel = contentService.getDetail(id);
            log.info("# api - content detail page = {}", contentModel);
            return super.ok(new BaseApiDto<>(contentModel));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "조회 실패 : " + e.getMessage());
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseApiDto<?>> register(@ModelAttribute ContentModel model) {
        //저장 권한 검사
//        if (!userInfo.isLogin()) {
//            throw new BizException(ResultCode.HTTP_403.setErrMsg("저장 권한이 없습니다."));
//        }
        try {
            LocalDate now = LocalDate.now();
            String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String key = BASE_UPLOAD_DIR + "/" +  model.getUserId() +  "/" + today;
            contentService.add(model, key);
            return ResponseEntityUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "저장 실패 : " + e.getMessage());
        }
    }

    @PostMapping(path = "/modify", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseApiDto<?>> modify(@ModelAttribute ContentModel contentDetail) throws Exception {
        try {
            log.info("# Content modify API = {}", contentDetail);
            LocalDate now = LocalDate.now();
            String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String key = BASE_UPLOAD_DIR + "/" +  contentDetail.getUserId() +  "/" + today;
            contentService.modifyContent(contentDetail, key);
            return ResponseEntityUtil.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "컨텐츠 수정 실패 : " + e.getMessage());
        }
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<BaseApiDto<?>> delete(@PathVariable("contentId") Long contentId) throws Exception {
//        // 관리자 권한이 있거나 본인이 올린 파일인 경우만 삭제
//        if ( userInfo.hasRole(UserRole.SYS_ADMIN)
//                || userInfo.hasRole(UserRole.ADMIN)
//                || awsS3.getKey().contains(userInfo.getId())) {
//            awsS3Service.remove(awsS3);
//            return ResponseEntityUtil.ok();
//        }
        contentService.remove(contentId);
        return ResponseEntityUtil.ok();
    }
}
