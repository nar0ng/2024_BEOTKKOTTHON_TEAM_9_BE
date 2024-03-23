package com.example.bommeong.biz.user.service;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.adopt.repository.AdoptRepository;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.example.bommeong.biz.user.repository.AuthorityRepository;
import com.example.bommeong.common.dto.PageEntity;
import com.example.bommeong.common.security.JwtProvider;
import com.example.bommeong.common.security.SecurityUtil;
import com.example.bommeong.common.security.Token;
import com.example.bommeong.common.security.TokenRepository;
import com.example.bommeong.biz.user.domain.User;
import com.example.bommeong.biz.user.dto.UserDtoRes;
import com.example.bommeong.biz.user.repository.UserRepository;
import com.example.bommeong.common.service.BaseServiceImplWithJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor //생성자 주입
public class UserService extends BaseServiceImplWithJpa {
    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtProvider jwtProvider;

    private final AdoptRepository adoptRepository;


    public Optional<User> test(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);
    }


    public void signUp(UserDtoReq.SignUpDto signUpDto) throws Exception {
        // 이미 회원이면 가입 취소
        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }

        String encodedPw = passwordEncoder.encode(signUpDto.getPassword());

        signUpDto.setPassword((encodedPw));

        User user = User.createUser(signUpDto, encodedPw);

        userRepository.save(user);


    }

    public UserDtoRes.TokenDto login(UserDtoReq.LoginDto loginDto) {
        Optional<User> findUser = userRepository.findByEmail(loginDto.getEmail());

        if (findUser.isEmpty()) {
            throw new RuntimeException("가입되지 않은 회원입니다.");
        }

        User user = findUser.get();


        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) { // 비밀번호 일치
            String refreshToken;

            if(user.getRefreshToken() != null){
                refreshToken = user.getRefreshToken();
            }
            else{
                refreshToken = createRefreshToken(user);
                user.setRefreshToken(refreshToken);
                userRepository.save(user);
            }
/*
            Authority authority = Authority.createAuthority(user);
            //user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            authorityRepository.save(authority);

 */
            return UserDtoRes.TokenDto.builder()
                    .access_token(jwtProvider.createToken(user.getEmail(), user.getRoles()))
                    .refresh_token(refreshToken)
                    .name(user.getName())
                    .email(user.getEmail())
                    .memberId(user.getId())
                    .memberType(user.getMemberType())
                    .build();
        }

        throw new RuntimeException("아이디 또는 패스워드가 잘못되었습니다.");
    }


    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(user.getId())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(31536000) //1년
                        .build()
        );
        return token.getRefresh_token();
    }

    public Token validRefreshToken(User user, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(user.getId()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getExpiration() < 1) { ///tntntntntntntnt
            return null;
        } else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
            if(token.getExpiration() < 100) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    public UserDtoRes.TokenDto refreshAccessToken(UserDtoRes.TokenDto token) throws Exception {
        String account = jwtProvider.getEmail(token.getAccess_token());
        User user = userRepository.findByEmail(account).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        Token refreshToken = validRefreshToken(user, token.getRefresh_token());

        if (refreshToken != null) {
            return UserDtoRes.TokenDto.builder()
                    .access_token(jwtProvider.createToken(account, user.getRoles()))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            throw new Exception("로그인을 해주세요");
        }
    }

    public void updateUser(UserDtoReq.UpdateDto updateDto) {
        Optional<User> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);

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
        Optional<User> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);

        if (user.isEmpty()){
            throw new RuntimeException("유저가 존재하지 않습니다");
        }

        user.get().setMemberType("N");

        userRepository.save(user.get());
    }

    public UserDtoRes.UserRes getUser() {
        Optional<User> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByEmail);

        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다");
        }

        return UserDtoRes.UserRes.builder()
                .user(user.get())
                .build();
    }

    public UserDtoRes.MyPageDto getMyPage(Long memberId) {
        // 유저 존재 확인
        Optional<User> user = userRepository.findById(memberId);
        if (user.isEmpty()) throw new RuntimeException("user not found");

        // 유저 입양 신청 확인
        Optional<AdoptEntity> adoptEntity = adoptRepository.findByUser(user.get());



        return new UserDtoRes.MyPageDto(user.get(), adoptEntity);
    }

    public PageEntity<UserDtoRes.UserListAdmin> getAllUserForAdmin(PageEntity<UserDtoRes.UserListAdmin> pageEntity) throws Exception {
        Optional<AdoptEntity> adoptModel = Optional.of(new AdoptEntity());
        Page<User> page = userRepository.findAll(toPageable(pageEntity));
        Stream<UserDtoRes.UserListAdmin> stream = page.getContent().stream().map(UserDtoRes.UserListAdmin::new);

        pageEntity.setTotalCnt(page.getTotalElements());
        pageEntity.setDtoList(stream.toList());
        return pageEntity;
    }
}