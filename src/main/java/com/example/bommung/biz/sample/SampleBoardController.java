package com.example.bommung.biz.sample;

import com.example.bommung.biz.sample.dto.SampleBoardModel;
import com.example.bommung.common.code.ResultCode;
import com.example.bommung.common.dto.PageEntity;
import com.example.bommung.common.exception.BizException;
import com.example.bommung.common.user.UserInfo;
import com.example.bommung.common.utils.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/sample/board")
public class SampleBoardController {


    private final SampleBoardServiceWithJpa sampleBoardService;
//    private final SampleBoardServiceWithMapper sampleBoardService;

    //@Override
    @GetMapping("")
    public ModelAndView getList(Pageable page) throws Exception {
        log.debug("page = {}", page);

        // TODO 검색 기능 추가
        PageEntity<SampleBoardModel> pageEntity = new PageEntity(page);

        PageEntity<SampleBoardModel> boardPage = sampleBoardService.getList(pageEntity);


        ModelAndView mv = new ModelAndView("sample/board/list");


        mv.addObject("boardList", boardPage.getDtoList());
        mv.addObject("totalCnt", boardPage.getTotalCnt());

        return mv;

    }

    //@Override
    @GetMapping("/{boardNo}")
    public ResponseEntity<SampleBoardModel> getDetail (@PathVariable("boardNo") Long boardNo) throws Exception {
        log.debug("# getDetail() : boardNo = {} ", boardNo);

        SampleBoardModel sampleBoardModel = sampleBoardService.getDetail(boardNo);
        return ResponseEntityUtil.ok(sampleBoardModel);
    }

    /**
     * 게시판 생성
     *      - sampleBoardModel.boardNo 값이 없으면 생성, 있으면 수정
     */
    //@Override
    @PostMapping("")
    public ResponseEntity<SampleBoardModel> save(
            @RequestBody SampleBoardModel param, @ModelAttribute("userInfo") UserInfo userInfo) throws Exception {

        log.debug("# save() : body = {} ", param);

        // 파라메터로 값이 넘어와도 null로 치환
        param.setBoardNo(null);
        param.setOwnerId(userInfo.getId());

        // DB 저장
        return ResponseEntityUtil.ok(sampleBoardService.add(param));
    }

    //@Override
    @PutMapping("/{boardNo}")
    public ResponseEntity<SampleBoardModel> modify(
            @PathVariable("boardNo") Long boardNo,
            @RequestBody SampleBoardModel param,
            @ModelAttribute("userInfo") UserInfo userInfo) throws Exception {

        log.debug("# modify() : boardNo = {}, body = {} ", boardNo, param);

        // 수정 대상 데이터 조회
        SampleBoardModel sampleBoardModel = sampleBoardService.getDetail(boardNo);

        log.debug("# sampleBoardModel1 : {} ", sampleBoardModel);

        // 권한 검사
        Optional.ofNullable(sampleBoardModel.getOwnerId())
                .filter(s -> s.equals(userInfo.getId()))
                .orElseThrow(() -> new BizException(ResultCode.HTTP_403, "본인만 수정 가능"));

        // 수정 항목 맵핑
        Optional.ofNullable(param.getBoardName()).ifPresent(s -> sampleBoardModel.setBoardName(s));

        return ResponseEntityUtil.ok(sampleBoardService.modify(sampleBoardModel));
    }

    //@Override
    @DeleteMapping("/{boardNo}")
    public ResponseEntity remove(@PathVariable("boardNo") Long boardNo, @ModelAttribute("userInfo") UserInfo userInfo) throws Exception {
        log.debug("# remove() : boardNo = {}, body = {} ", boardNo);

        SampleBoardModel sampleBoardModel = sampleBoardService.getDetail(boardNo);

        // 권한 검사
        Optional.ofNullable(sampleBoardModel.getOwnerId())
                .filter(s -> s.equals(userInfo.getId()))
                .orElseThrow(() -> new BizException(ResultCode.HTTP_403, "본인만 삭제 가능"));

        sampleBoardService.remove(boardNo);
        return ResponseEntityUtil.ok();
    }
}
