package com.example.bommeong.biz.content.service;

import com.example.bommeong.biz.content.dto.*;
import com.example.bommeong.biz.content.repository.ContentPageRepository;
import com.example.bommeong.biz.content.repository.ContentRepository;
import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.content.repository.HistoryRepository;
import com.example.bommeong.common.code.ResultCode;
import com.example.bommeong.common.dto.PageEntity;
import com.example.bommeong.common.exception.BizException;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class ContentService extends BaseServiceImplWithJpa<ContentModel, ContentEntity, Long, ContentRepository> {

    private final ContentPageRepository contentPageRepository;

    private final HistoryRepository historyRepository;

    private final AwsS3Service awsS3Service;


    public ContentService(ContentRepository contentRepository, ContentPageRepository contentPageRepository, HistoryRepository historyRepository, AwsS3Service awsS3Service) {
        this.repository = contentRepository;
        this.contentPageRepository = contentPageRepository;
        this.historyRepository = historyRepository;

        this.awsS3Service = awsS3Service;
    }

    @Override
    public PageEntity<ContentModel> getList(PageEntity<ContentModel> pageEntity) throws Exception {
        Page<ContentEntity> page = repository.findAll(toPageable(pageEntity));

        Stream<ContentModel> stream = page.getContent().stream()
                .map(entity -> entity.toModel());

        pageEntity.setTotalCnt(page.getTotalElements());
        pageEntity.setDtoList(stream.toList());
        return pageEntity;
    }

    public List<ContentModel> getListByViewYn(int pagePerCnt) throws Exception {
        Sort sort = Sort.by(Sort.Order.desc("shortYn"));


        return repository.findByViewYn("Y", sort).stream().map(ContentModel::new).toList();
    }

    public List<HistoryModel> getListByViewCount(int pagePerCnt) throws Exception {
//        return repository.findAllByOrderByViewCountDesc().stream().map(ContentModel::new).toList();
        return historyRepository.countContentIdOccurrence1s().stream().map(HistoryModel::new).toList();
    }

    public List<ContentModel> search(String keyword) throws Exception {
        Sort sort = Sort.by(Sort.Order.desc("shortYn"));
        return repository.findAllByTitleContainingOrDescriptionContaining(keyword, keyword, sort).stream().map(ContentModel::new).toList();
    }

    public List<ContentModel> getListByUserId(Long userId) throws Exception {
        return repository.findByUserId(userId).stream().map(ContentModel::new).toList();
    }

    @Override
    @Transactional
    public ContentModel getDetail(Long pk) throws Exception {
        // 조회수 증가 (쿠키 또는 세션으로 체크?
        repository.updateViewCount(pk);
        // 히스토리 저장
        historyRepository.save(new HistoryEntity(pk));
        return super.getDetail(pk);
    }


    public ContentModel getDetailAdmin(Long pk) throws Exception {
//        // 조회수 증가 (쿠키 또는 세션으로 체크?
//        repository.updateViewCount(pk);
//        // 히스토리 저장
//        historyRepository.save(new HistoryEntity(pk));
        return super.getDetail(pk);
    }

    @Transactional
    public ContentModel add(ContentModel model, String dirName) throws Exception {
        ContentEntity contentEntity = model.toEntity();
        List<ContentPageEntity> contentPageEntityList = new ArrayList<>();


        //페이지 저장 루프
        for (ContentPageEntity contentPageEntity : contentEntity.getContentPageEntityList()) {
            log.info("In add ContentPage Loop : contentPageEntity = {}", contentPageEntity);
            //페이지 별 파일 저장
//            contentPageEntity.setUrl(storageService.store(contentPageEntity.getUploadFile(), model.getUserId()));
            AwsS3Dto awsS3Dto1 = awsS3Service.upload(contentPageEntity.getUploadFile(), dirName);
            contentPageEntity.setUrl(awsS3Dto1.getPath());
            contentPageEntity.setOriginalName(awsS3Dto1.getKey());
            contentPageEntity.setSize(contentPageEntity.getUploadFile().getSize());

            contentPageEntityList.add(contentPageEntity);
            contentPageEntity.setContentEntity(contentEntity);
        }

        AwsS3Dto awsS3Dto = awsS3Service.upload(model.getUploadFile(), dirName);
        contentEntity.setCompanyImg(awsS3Dto.getPath());
        contentEntity.setCompanyImgName(awsS3Dto.getKey());

        contentEntity.getContentPageEntityList().addAll(contentPageEntityList);

        repository.save(contentEntity);
        return new ContentModel();
    }

    @Transactional
    public void modifyContent(ContentModel model, String dirName) throws Exception {
        //업로드 된 파일 이름 -- 롤백용
        List<String> uploadedFileName = new ArrayList<>();
        //제거할 파일 이름
        List<String> deleteFileName = new ArrayList<>();
        try {
            ContentEntity content = repository.findById(model.getContentId())
                    .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+ model.getContentId()));

            ContentEntity contentEntity = model.toEntity();
            contentEntity.setViewYn(content.getViewYn());
            contentEntity.setViewCount(content.getViewCount());
            List<ContentPageEntity> contentPageEntityList = new ArrayList<>();

            //페이지 저장 루프
            for (ContentPageEntity contentPageEntity : contentEntity.getContentPageEntityList()) {
                log.info("In add ContentPage Loop : contentPageEntity = {}", contentPageEntity);
                //기존 페이지 수정
                if (contentPageEntity.getPageId() != null) {
                    ContentPageEntity contentPage = contentPageRepository.findById(contentPageEntity.getPageId())
                            .orElseThrow(()-> new IllegalArgumentException("해당 페이지가 없습니다. pageId="+ contentPageEntity.getPageId()));
                    if (!Objects.equals(contentPageEntity.getPageId(), contentPage.getPageId())) {
                        throw new IOException("잘못된 페이지 아이디입니다.");
                    }
                    //페이지 이미지 업로드 파일이 있으면 지우고 새로 만듦
                    if (contentPageEntity.getUploadFile() != null) {
                        //이전 데이터 저장
                        deleteFileName.add(contentPage.getOriginalName());
                        
                        AwsS3Dto awsS3Dto1 = awsS3Service.upload(contentPageEntity.getUploadFile(), dirName);
                        contentPageEntity.setUrl(awsS3Dto1.getPath());
                        contentPageEntity.setOriginalName(awsS3Dto1.getKey());
                        contentPageEntity.setSize(contentPageEntity.getUploadFile().getSize());
                        uploadedFileName.add(awsS3Dto1.getKey());
                    } else {
                        contentPageEntity.setUrl(contentPage.getUrl());
                        contentPageEntity.setOriginalName(contentPage.getOriginalName());
                        contentPageEntity.setSize(contentPage.getSize());
                    }
                } else {
                    //새로운 페이지 생성
                    AwsS3Dto awsS3Dto1 = awsS3Service.upload(contentPageEntity.getUploadFile(), dirName);
                    contentPageEntity.setUrl(awsS3Dto1.getPath());
                    contentPageEntity.setOriginalName(awsS3Dto1.getKey());
                    contentPageEntity.setSize(contentPageEntity.getUploadFile().getSize());
                    uploadedFileName.add(awsS3Dto1.getKey());
                }

                contentPageEntityList.add(contentPageEntity);
                contentPageEntity.setContentEntity(contentEntity);
            }

            //회사 이미지 업로드 파일이 있으면 지우고 새로 만듦
            if (model.getUploadFile() != null) {
                deleteFileName.add(content.getCompanyImgName());

                AwsS3Dto awsS3Dto = awsS3Service.upload(model.getUploadFile(), dirName);
                contentEntity.setCompanyImg(awsS3Dto.getPath());
                contentEntity.setCompanyImgName(awsS3Dto.getKey());
                uploadedFileName.add(awsS3Dto.getKey());
            } else {
                contentEntity.setCompanyImg(content.getCompanyImg());
                contentEntity.setCompanyImgName(content.getCompanyImgName());
            }

            contentEntity.getContentPageEntityList().addAll(contentPageEntityList);

            repository.save(contentEntity);
            log.info("저장 완료 : 지울 파일 목록 = {}", deleteFileName);
            // 문제 없이 전부 수정 후 이전 파일 제거
            for (String fileName : deleteFileName) {
                awsS3Service.remove(new AwsS3Dto(fileName, ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            //에러 발생 시 업로드 된 이미지 전부 제거
            log.info("에러 발생 : 업로드 된 파일 롤백 = {}", uploadedFileName);
            for (String fileName : uploadedFileName) {
                awsS3Service.remove(new AwsS3Dto(fileName, ""));
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void remove(Long contentId) throws Exception {
        ContentEntity content = repository.findById(contentId)
                .orElseThrow(() -> new BizException(ResultCode.DATA_NOT_FOUND));

        //페이지 이미지 삭제 루프
        for (ContentPageEntity contentPageEntity : content.getContentPageEntityList()) {
            AwsS3Dto awsS3Dto1 = new AwsS3Dto(contentPageEntity.getOriginalName(), "");
            awsS3Service.remove(awsS3Dto1);
        }
        //컨텐츠 회사 이미지 제거
        AwsS3Dto awsS3Dto = new AwsS3Dto(content.getCompanyImgName(), "");
        awsS3Service.remove(awsS3Dto);
        repository.deleteById(contentId);
    }

}
