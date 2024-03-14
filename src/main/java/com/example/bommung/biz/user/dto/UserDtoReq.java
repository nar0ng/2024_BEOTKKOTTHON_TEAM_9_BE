package com.example.bommung.biz.user.dto;

import lombok.*;

public class UserDtoReq {
    @Getter
    @Setter
    @ToString
    public static class SignUpDto{
        private String email;
        private String password;

        private String name;

        private String phone;

        private String passwordConfirm;

        private String verificationCode;


        @Builder
        public SignUpDto(String userId, String password, String name, String phone){
            this.email = userId;
            this.password = password;
            this.name = name;
            this.phone = phone;
        }

    }

    @Getter
    public static class PhoneDto{
        private String phone;

    }

    @Getter
    public static class PhoneCheckDto{
        private String phone;
        private String cer;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginDto{
        private String email;
        private String password;



        @Builder
        public LoginDto(String email, String password) {
            this.email = email;
            this.password = password;
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateDto{
        private String name;
        private String phone;

    }
}