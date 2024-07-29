package com.example.bommeong.biz.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

public class ShelterDtoReq {

    @Getter
    @Setter
    @ToString
    public static class SignUpDto {
        private String email;
        private String password;
        private String name;
        private String phone;
        private String managerName;
        private String subEmail;
        private String docName;
        private String docUrl;
        private String address;
        private Double latitude;
        private Double longitude;
        private MultipartFile uploadFile;

    }

    @Getter
    @Setter
    public static class LoginDto {
        private String email;
        private String password;
    }
}
