package com.example.bommeong.biz.user.dto;

import com.example.bommeong.biz.user.domain.ShelterEntity;
import lombok.Getter;
import lombok.Setter;

public class ShelterDtoRes {
    @Getter
    @Setter
    public static class ShelterInfo {
        private Long shelterId;
        private String email;
        private String name;
        private String phone;
        private String managerName;
        private String subEmail;
        private String docName;
        private String docUrl;
        private String address;

        public ShelterInfo(ShelterEntity entity) {
            this.shelterId = entity.getId();
            this.email = entity.getEmail();
            this.name = entity.getName();
            this.phone = entity.getPhone();
            this.managerName = entity.getManagerName();
            this.subEmail = entity.getSubEmail();
            this.docName = entity.getDocName();
            this.docUrl = entity.getDocUrl();
            this.address = entity.getAddress();
        }
    }
}
