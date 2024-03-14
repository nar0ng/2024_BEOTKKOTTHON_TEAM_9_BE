package com.example.bommeong.biz.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Authority")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    @JoinColumn(name = "email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public static Authority createAuthority(User user) {
        final Authority authority = Authority.builder()
                .name("ROLE_USER")
                .user(user)
                .build();
        return authority;
    }
}