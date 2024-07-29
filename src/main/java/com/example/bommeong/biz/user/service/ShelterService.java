package com.example.bommeong.biz.user.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.user.domain.ShelterEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.biz.user.dto.CustomUserDetails;
import com.example.bommeong.biz.user.dto.ShelterDtoReq;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.example.bommeong.biz.user.dto.UserDtoRes;
import com.example.bommeong.biz.user.repository.ShelterRepository;
import com.example.bommeong.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
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
        userService.signUp(signUpDto1);

        // 이미 가입된 보호소면 가입 취소
        if (shelterRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 보호소입니다.");
        }
        
        // s3 업로드
        AwsS3Dto awsS3Dto = awsS3Service.upload(signUpDto.getUploadFile(), dirName);


        String encodedPw = passwordEncoder.encode(signUpDto.getPassword());

        signUpDto.setPassword((encodedPw));

        ShelterEntity shelterEntity = ShelterEntity.builder()
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


}
