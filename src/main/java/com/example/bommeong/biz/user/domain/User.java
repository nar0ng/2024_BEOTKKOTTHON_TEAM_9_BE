package com.example.bommeong.biz.user.domain;

import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String refreshToken;

    private DateTime created_at;

    private DateTime updated_at;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private boolean userType;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();



    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }

/*
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Inquiry> inquirys = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Inquiry> inquirys = new ArrayList<>();

    public void setInquirys(List<Inquiry> inquirys) {
        this.inquirys = inquirys;
        inquirys.forEach(o -> o.setUser(this));
    }
*/
    public void setRefreshToken(String refreshToken) { // 추가!
        this.refreshToken = refreshToken;
    }

    public static User createUser(UserDtoReq.SignUpDto signUpDto, String password) {
        final User user = User.builder()
                .email(signUpDto.getEmail())
                .password(password)
                .name(signUpDto.getName())
                .phone(signUpDto.getPhone())
                .refreshToken(null)
                .updated_at(null)
                .point(0)
                .userType(true)
                .build();
        return user;
    }
}