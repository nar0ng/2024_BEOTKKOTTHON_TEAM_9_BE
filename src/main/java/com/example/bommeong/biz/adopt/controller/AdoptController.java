package com.example.bommeong.biz.adopt.controller;

import com.example.bommeong.biz.adopt.dto.AdoptApplicationModel;
import com.example.bommeong.biz.adopt.dto.AdoptApplicationStatusDto;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.adopt.service.AdoptService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/adopt")
@Tag(name = "Adopt", description = "입양 API")
public class AdoptController extends BaseApiController<BaseApiDto<?>> {

    private final AdoptService adoptService;

    private final String BASE_UPLOAD_DIR = "adopt";

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "입양 신청", description = "")
    public ResponseEntity<BaseApiDto<?>> addAdopt(AdoptModel model) {
        try {
            log.info("data = {}", model);
            LocalDate now = LocalDate.now();
            String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String dirName = BASE_UPLOAD_DIR + "/" +  model.getPostId() +  "/" + today;
            adoptService.add(model, dirName);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            e.printStackTrace();
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "입양 신청 실패 : " + e.getMessage());
        }
    }

    @PutMapping("/status")
    public ResponseEntity<BaseApiDto<?>> updateAdoptApplicationStatus(@RequestBody AdoptApplicationStatusDto statusDto){
        adoptService.updateAdoptApplicationStatus(statusDto);
        return super.ok(BaseApiDto.newBaseApiDto());
    }

}
