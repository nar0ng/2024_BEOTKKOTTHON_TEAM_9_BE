package com.example.bommeong.biz.user.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import com.example.bommeong.biz.adopt.dao.AdoptApplicationStatus;
import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.dto.AdoptApplicantDetailsDto;
import com.example.bommeong.biz.adopt.dto.AdoptApplicantDto;
import com.example.bommeong.biz.adopt.dto.AdoptApplicationModel;
import com.example.bommeong.biz.adopt.repository.AdoptRepository;
import com.example.bommeong.biz.post.dao.BomInfoEntity;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dao.PostStatus;
import com.example.bommeong.biz.post.repository.PostRepository;
import com.example.bommeong.biz.user.domain.ShelterEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.biz.user.dto.*;
import com.example.bommeong.biz.user.repository.ShelterRepository;
import com.example.bommeong.common.security.SecurityUtil;
import com.example.bommeong.jwt.JWTUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final PostRepository postRepository;
    private final AdoptRepository adoptRepository;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service awsS3Service;
    private final AuthenticationConfiguration configuration;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final UserService userService;

    @Transactional
    public void signUp(ShelterDtoReq.SignUpDto signUpDto, String dirName) throws Exception {
        // user 생성
        UserDtoReq.SignUpDto signUpDto1 = new UserDtoReq.SignUpDto(
                signUpDto.getEmail(),
                signUpDto.getPassword(),
                signUpDto.getName(),
                signUpDto.getPhone(),
                "ROLE_SHELTER"
        );
        UserEntity userEntity = userService.signUp(signUpDto1);

        // 이미 가입된 보호소면 가입 취소
        if (shelterRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 보호소입니다.");
        }
        
        // s3 업로드
        AwsS3Dto awsS3Dto = awsS3Service.upload(signUpDto.getUploadFile(), dirName);


        String encodedPw = passwordEncoder.encode(signUpDto.getPassword());

        signUpDto.setPassword((encodedPw));

        ShelterEntity shelterEntity = ShelterEntity.builder()
                .id(userEntity.getId())
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .name(signUpDto.getName())
                .phone(signUpDto.getPhone())
                .managerName(signUpDto.getManagerName())
                .subEmail(signUpDto.getSubEmail())
                .docName(awsS3Dto.getKey())
                .docUrl(awsS3Dto.getPath())
                .address(signUpDto.getAddress())
                .latitude(signUpDto.getLatitude())
                .longitude(signUpDto.getLongitude())
                .build();

        shelterRepository.save(shelterEntity);

    }

    public UserDtoRes.TokenDto login(UserDtoReq.LoginDto loginDto) throws Exception {
        Optional<ShelterEntity> findShelter = shelterRepository.findByEmail(loginDto.getEmail());
        // 보호소 존재 여부 체크
        if (findShelter.isEmpty()) {
            throw new RuntimeException("가입되지 않은 보호소입니다.");
        }

        // 로그인 인증
        return userService.login(loginDto);
    }

    public List<BomListDto> findAllBomListByShelterId(Long shelterId){
        List<PostEntity> posts = postRepository.findAllByShelterId(shelterId);

        return posts.stream()
                .map(post -> {
                    BomInfoEntity bomInfo = post.getBomInfoEntity();
                    int adoptStatusCount = post.getAdoptEntity().size();
                    if (bomInfo == null) {
                        log.warn("BomInfoEntity is null for PostEntity with ID: {}", post.getPostId());
                        return BomListDto.builder()
                                .postId(post.getPostId())
                                .name("Unknown")
                                .breed("Unknown")
                                .gender("Unknown")
                                .extra("No info available")
                                .createdAt(LocalDateTime.now())
                                .adoptStatusCount(0)
                                .build();
                    }
                    log.debug("Post ID: {}, BomInfoEntity: {}", post.getPostId(), bomInfo);
                    return BomListDto.builder()
                            .postId(post.getPostId())
                            .name(bomInfo.getName())
                            .breed(bomInfo.getBreed())
                            .gender(bomInfo.getGender())
                            .extra(bomInfo.getExtra())
                            .createdAt(post.getCreatedAt())
                            .adoptStatusCount(adoptStatusCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 보호소의 입양 현황을 조회
    public AdoptionStatusDto getAdoptionStatsByShelterId(Long shelterId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        int totalDogsCount = postRepository.countByShelterId(shelterId);
        int todayAdoptionRequests = adoptRepository.countTodayAdoptionRequests(shelterId, startOfDay, endOfDay);
        int completedAdoptions = postRepository.countByShelterIdAndStatus(shelterId, PostStatus.COMPLETED);
        int pendingAdoptions = postRepository.countByShelterIdAndStatus(shelterId, PostStatus.BEFORE);
        return AdoptionStatusDto.builder()
                .totalDogsCount(totalDogsCount)
                .todayAdoptionRequests(todayAdoptionRequests)
                .completedAdoptions(completedAdoptions)
                .pendingAdoptions(pendingAdoptions)
                .build();
    }

    public List<AdoptApplicantDto> findAdoptionApplicationsByPostId(Long postId) {
        return adoptRepository.findByPostPostId(postId)
                .stream()
                .map(adoptEntity -> {
                    UserEntity user = adoptEntity.getUser();
                    AdoptApplicationEntity application = adoptEntity.getAdoptApplicationEntity();
                    return new AdoptApplicantDto(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            application.getReasonForAdoption()
                    );
                })
                .collect(Collectors.toList());
    }


    public AdoptApplicantDetailsDto getAdoptApplicantDetails(Long postId, Long memberId) {
        Optional<AdoptEntity> adoptEntityOptional = adoptRepository.findByPostPostIdAndUserId(postId, memberId);
        if (adoptEntityOptional.isEmpty()) {
            throw new RuntimeException("Adopt entity not found for postId: " + postId + " and memberId: " + memberId);
        }

        AdoptEntity adoptEntity = adoptEntityOptional.get();
        UserEntity user = adoptEntity.getUser();
        AdoptApplicationEntity application = adoptEntity.getAdoptApplicationEntity();
        AdoptApplicationModel applicationModel = new AdoptApplicationModel(application);

        return new AdoptApplicantDetailsDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                applicationModel
        );
    }


    public ShelterDtoRes.ShelterInfo getShelterInfo(Long shelterId) {
        ShelterEntity shelterEntity = shelterRepository.findById(shelterId).orElseThrow(() -> new RuntimeException("보호소를 찾을 수 없습니다. "));
        return new ShelterDtoRes.ShelterInfo(shelterEntity);

    }

    public void updateShelterInfo(ShelterDtoReq.UpdateShelterInfoDto updateShelterInfoDto) {
        ShelterEntity shelterEntity = shelterRepository.findById(updateShelterInfoDto.getShelterId()).orElseThrow(() -> new RuntimeException("보호소를 찾을 수 없습니다. "));

        shelterEntity.updateInfo(updateShelterInfoDto);
        shelterRepository.save(shelterEntity);

    }
}
