package com.example.bommeong.biz.shelter.controller;

import com.example.bommeong.biz.shelter.dto.BomListDto;
import com.example.bommeong.biz.shelter.service.ShelterService;
import com.example.bommeong.common.controller.BaseApiController;
import com.example.bommeong.common.controller.BaseApiDto;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/shelter")
@Tag(name = "Shelter", description = "보호소 API")
public class ShelterController extends BaseApiController<BaseApiDto<?>> {

    private final ShelterService shelterService;

    @GetMapping("/bom-lists/{shelterId}")
    public ResponseEntity<BaseApiDto<?>> findAllBomLists(@PathVariable Long shelterId) {
        try {
            List<BomListDto> bomList = shelterService.findAllBomListByShelterId(shelterId);

            return super.ok(new BaseApiDto<>(bomList));
        } catch (Exception e) {
            return super.fail(BaseApiDto.newBaseApiDto(), "9999", "보호견 리스트 조회 실패 : " + e.getMessage());
        }
    }


}
