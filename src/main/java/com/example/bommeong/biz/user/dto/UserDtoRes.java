package com.example.bommeong.biz.user.dto;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.user.domain.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserDtoRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenDto {
        private String access_token;
        private String refresh_token;
        private String name;
        private String email;
        private Long memberId;
        private String memberType;
    }

    @Getter
    public static class UserRes {
        private String email;

        private String name;

        private String phone;

        private Integer point;
        private String memberType;

        @Builder
        public UserRes(UserEntity userEntity) {
            this.email = userEntity.getEmail();
            this.name = userEntity.getName();
            this.phone = userEntity.getPhone();
            this.memberType = userEntity.getMemberType();
        }
    }

    @Data
    @NoArgsConstructor
    public static class MyPageDto {
        private Long memberId;
        private String name;
        private String email;
        private String memberType;
        private AdoptModel adoption;

        public MyPageDto(UserEntity userEntity, Optional<AdoptEntity> adoptEntity) {
            this.memberId = userEntity.getId();
            this.name = userEntity.getName();
            this.email = userEntity.getEmail();
            this.memberType = userEntity.getMemberType();
            adoptEntity.ifPresent(entity -> this.adoption = entity.toModel());
        }
    }

    @Data
    @NoArgsConstructor
    public static class UserListAdmin {
        private Long memberId;
        private String name;
        private String email;
        private String phone;
        private String memberType;
        private String memberStatus;
        private AdoptModel adoption;
        private LocalDateTime createdAt;


        public UserListAdmin(UserEntity userEntity) {
            this.memberId = userEntity.getId();
            this.name = userEntity.getName();
            this.email = userEntity.getEmail();
            this.phone = userEntity.getPhone();
            this.memberType = userEntity.getMemberType();
            this.memberStatus = userEntity.getMemberStatus();
            this.createdAt = userEntity.getCreatedAT();
//                adoptEntity.ifPresent(entity -> this.adoption = entity.toModel());
        }

    }

}
