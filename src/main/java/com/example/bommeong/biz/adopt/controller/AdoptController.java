package com.example.bommeong.biz.adopt.controller;

import com.example.bommeong.biz.adopt.dto.AdoptApplicationStatusDto;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.adopt.service.AdoptService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import com.example.bommeong.common.dto.PageEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "입양 신청", description = "입양 신청")
    public ResponseEntity<BaseApiDto<?>> addAdopt(AdoptModel model) {
        try {
            log.info("data = {}", model);
            LocalDate now = LocalDate.now();
            String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String dirName = BASE_UPLOAD_DIR + "/" +  model.getPostId() +  "/" + today;
            adoptService.add(model, dirName);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            log.error("입양 신청 실패 : {}", e.getMessage());
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "입양 신청 실패 : " + e.getMessage());
        }
    }

    @PutMapping("/status")
    @Operation(summary = "입양 신청 상태 변경", description = "입양 신청 상태 변경")
    public ResponseEntity<BaseApiDto<?>> updateAdoptApplicationStatus(@RequestBody AdoptApplicationStatusDto statusDto){
        adoptService.updateAdoptApplicationStatus(statusDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "입양 신청 목록 조회", description = "입양 신청 목록 조회")
    public ResponseEntity<BaseApiDto<?>> getAdoptList(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) String searchValue) {
        try {
            log.info("# get adopt list");
            return super.ok(new BaseApiDto<>(adoptService.getAdoptList()));
        } catch (Exception e) {
            log.error("입양 신청 목록 조회 실패 : {}", e.getMessage());
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "입양 신청 목록 조회 실패 : " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "입양 신청 삭제", description = "입양 신청 삭제")
    public ResponseEntity<BaseApiDto<?>> deleteAdopt(@PathVariable Long id) {
        try {
            log.info("# delete adopt id = {}", id);
            adoptService.deleteAdopt(id);
            return super.ok(BaseApiDto.newBaseApiDto());
        } catch (Exception e) {
            log.error("입양 신청 삭제 실패 : {}", e.getMessage());
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "입양 신청 삭제 실패 : " + e.getMessage());
        }

    }

}
