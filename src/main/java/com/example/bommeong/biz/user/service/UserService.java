package com.example.bommeong.biz.user.service;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.repository.AdoptRepository;
import com.example.bommeong.biz.user.domain.UserEntity;
import com.example.bommeong.biz.user.dto.CustomUserDetails;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.example.bommeong.biz.user.repository.RefreshRepository;
import com.example.bommeong.common.dto.PageEntity;
import com.example.bommeong.common.security.SecurityUtil;
import com.example.bommeong.biz.user.dto.UserDtoRes;
import com.example.bommeong.biz.user.repository.UserRepository;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import com.example.bommeong.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor //생성자 주입
public class UserService extends BaseServiceImplWithJpa {
    private static long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000; // 30일을 밀리초로 변환

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration configuration;
    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;
    private final AdoptRepository adoptRepository;
    private final TokenService tokenService;


    public Optional<UserEntity> test(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);
    }


    public void signUp(UserDtoReq.SignUpDto signUpDto) throws Exception {
        // 이미 회원이면 가입 취소
        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }

        String encodedPw = passwordEncoder.encode(signUpDto.getPassword());

        signUpDto.setPassword((encodedPw));

        UserEntity userEntity = UserEntity.createUser(signUpDto, encodedPw);

        userRepository.save(userEntity);


    }

    public UserDtoRes.TokenDto login(UserDtoReq.LoginDto loginDto) throws Exception {
        Optional<UserEntity> findUser = userRepository.findByEmail(loginDto.getEmail());

        // 회원 존재 여부 체크
        if (findUser.isEmpty()) {
            throw new RuntimeException("가입되지 않은 회원입니다.");
        }

        // 로그인 인증
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword(), null);
        Authentication authentication = configuration.getAuthenticationManager().authenticate(authToken);
        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인 실패");
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


        UserEntity userEntity = findUser.get();

        return UserDtoRes.TokenDto.builder()
                .access_token(access)
                .refresh_token(refresh)
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .memberId(userEntity.getId())
                .memberType(userEntity.getMemberType())
                .build();

    }



//    public Token validRefreshToken(UserEntity userEntity, String refreshToken) throws Exception {
//        Token token = refreshRepository.findById(userEntity.getId()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
//        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
//        if (token.getExpiration() < 1) { ///tntntntntntntnt
//            return null;
//        } else {
//            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
//            if(token.getExpiration() < 100) {
//                token.setExpiration(1000);
//                refreshRepository.save(token);
//            }
//
//            // 토큰이 같은지 비교
//            if(!token.getRefresh_token().equals(refreshToken)) {
//                return null;
//            } else {
//                return token;
//            }
//        }
//    }

//    public UserDtoRes.TokenDto refreshAccessToken(UserDtoRes.TokenDto token) throws Exception {
//        String account = jwtUtil.getEmail(token.getAccess_token());
//        UserEntity userEntity = userRepository.findByEmail(account).orElseThrow(() ->
//                new BadCredentialsException("잘못된 계정정보입니다."));
//        Token refreshToken = validRefreshToken(userEntity, token.getRefresh_token());
//
//        if (refreshToken != null) {
//            return UserDtoRes.TokenDto.builder()
//                    .access_token(jwtUtil.createToken(account, userEntity.getRoles()))
//                    .refresh_token(refreshToken.getRefresh_token())
//                    .build();
//        } else {
//            throw new Exception("로그인을 해주세요");
//        }
//    }

    public void updateUser(UserDtoReq.UpdateDto updateDto) {
        Optional<UserEntity> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);

        if (user == null){
            throw new RuntimeException("유저가 존재하지 않습니다");
        }

        String name = updateDto.getName();
        String phone = updateDto.getPhone();

        if(name == null)
            name = user.get().getName();
        if(phone == null)
            phone = user.get().getPhone();

        user.get().setName(name);
        user.get().setPhone(phone);

        userRepository.save(user.get());
    }

    public void deleteUser() throws Exception {
        Optional<UserEntity> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);

        if (user.isEmpty()){
            throw new RuntimeException("유저가 존재하지 않습니다");
        }

        user.get().setMemberType("N");

        userRepository.save(user.get());
    }

    public UserDtoRes.UserRes getUser() {
        Optional<UserEntity> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다");
        }

        return UserDtoRes.UserRes.builder()
                .userEntity(user.get())
                .build();
    }

    public UserDtoRes.MyPageDto getMyPage(Long memberId) {
        // 유저 존재 확인
        Optional<UserEntity> user = userRepository.findById(memberId);
        if (user.isEmpty()) throw new RuntimeException("user not found");

        // 유저 입양 신청 확인
        Optional<AdoptEntity> adoptEntity = adoptRepository.findByUser(user.get());



        return new UserDtoRes.MyPageDto(user.get(), adoptEntity);
    }

    public PageEntity<UserDtoRes.UserListAdmin> getAllUserForAdmin(PageEntity<UserDtoRes.UserListAdmin> pageEntity) throws Exception {
        Optional<AdoptEntity> adoptModel = Optional.of(new AdoptEntity());
        Page<UserEntity> page = userRepository.findAll(toPageable(pageEntity));
        Stream<UserDtoRes.UserListAdmin> stream = page.getContent().stream().map(UserDtoRes.UserListAdmin::new);

        pageEntity.setTotalCnt(page.getTotalElements());
        pageEntity.setDtoList(stream.toList());
        return pageEntity;
    }


}