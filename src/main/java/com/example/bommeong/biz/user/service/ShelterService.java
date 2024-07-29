package com.example.bommeong.biz.user.service;

import com.example.bommeong.aws.s3.AwsS3Dto;
import com.example.bommeong.aws.s3.AwsS3Service;
import com.example.bommeong.biz.user.domain.ShelterEntity;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.biz.user.dto.CustomUserDetails;
import com.example.bommeong.biz.user.dto.ShelterDtoReq;
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

    @Transactional
    public void signUp(ShelterDtoReq.SignUpDto signUpDto, String dirName) throws IOException {
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
                .docUrl(awsS3Dto.getKey())
                .address(signUpDto.getAddress())
                .latitude(signUpDto.getLatitude())
                .longitude(signUpDto.getLongitude())
                .build();

        shelterRepository.save(shelterEntity);

    }

    public UserDtoRes.TokenDto login(ShelterDtoReq.LoginDto loginDto) throws Exception {
        Optional<ShelterEntity> findUser = shelterRepository.findByEmail(loginDto.getEmail());

        // 회원 존재 여부 체크
        if (findUser.isEmpty()) {
            throw new RuntimeException("가입되지 않은 보호소입니다.");
        }

        // 로그인 인증
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword(), null);
        Authentication authentication = configuration.getAuthenticationManager().authenticate(authToken);
        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인 인증 실패");
        }

        // UserDetails 클래스에서 유저(principal)를 가져옴 + 타입 캐스트
        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        // role 값을 이터레이터로 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();

        String role = authority.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, TokenService.REFRESH_TOKEN_VALIDITY);
        String refresh = jwtUtil.createJwt("refresh", username, role, TokenService.REFRESH_TOKEN_VALIDITY);

        //Refresh 토큰 저장
        tokenService.saveRefreshToken(username, refresh);

        ShelterEntity shelterEntity = findUser.get();
        
        
        return UserDtoRes.TokenDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .name(shelterEntity.getName())
                .email(shelterEntity.getEmail())
                .memberId(shelterEntity.getShelterId())
                .memberType("shelter")
                .build();
    }
}
