package com.example.bommeong.biz.user.domain;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dao.LikeEntity;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAT;

   @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAT;

    @Column(name = "member_status", nullable = false)
    private String memberStatus;

    @Column(name = "member_type", nullable = false)
    private String memberType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeEntity> likes = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdoptEntity adoptEntity;


//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonIgnore
//    @Builder.Default
//    private List<Authority> roles = new ArrayList<>();

    @Column(name = "ROLE")
    private String role;



//    public void setRoles(List<Authority> role) {
//        this.roles = role;
//        role.forEach(o -> o.setUser(this));
//    }

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
                .memberStatus("Y")
                .memberType(signUpDto.getMemberType())
                .build();
        return user;
    }
}