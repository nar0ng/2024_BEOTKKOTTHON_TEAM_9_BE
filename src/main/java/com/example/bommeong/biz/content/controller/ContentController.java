package com.example.bommeong.biz.content.controller;

import com.example.bommeong.biz.content.dto.ContentModel;
import com.example.bommeong.biz.content.service.ContentService;
import com.example.bommeong.common.dto.PageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/cms/content")
public class ContentController {
    private final ContentService contentService;
    private final String BASE_UPLOAD_DIR = "content";
    @ModelAttribute("ynMap")
    public Map<String, String> ynCd() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Y", "Y");
        map.put("N", "N");
        return map;
    }

    @GetMapping(path = {"", "/"})
    public String index(Pageable pageable, Model model) throws Exception {
        PageEntity<ContentModel> pageEntity = contentService.getList(new PageEntity<>(pageable));
        log.info("# pageEntity = {}", pageEntity);
        model.addAttribute("contentList", pageEntity);
        return "cms/content/index";
    }

    @GetMapping("/{contentId}")
    public String detail(@PathVariable("contentId") Long id, Model model) throws Exception {
        ContentModel contentModel = contentService.getDetailAdmin(id);
        log.info("# content detail page = {}", contentModel);
        model.addAttribute("contentDetail", contentModel);
        return "cms/content/detail";
    }

    @PostMapping(path = "/modify", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String modify(@ModelAttribute ContentModel contentDetail) throws Exception {
        log.info("# Content modify = {}", contentDetail);
        LocalDate now = LocalDate.now();
        String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String key = BASE_UPLOAD_DIR + "/" +  contentDetail.getUserId() +  "/" + today;
        contentService.modifyContent(contentDetail, key);
        return "redirect:/cms/content";
    }
}
