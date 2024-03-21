package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdoptApplicationModel {
    private String firstConsent;
    private String firstResponse;
    private String secondResponse;
    private String thirdResponse;
    private String fourthResponse;
    private String firstAdoptionResponse;
    private String secondAdoptionResponse;

    public AdoptApplicationModel(AdoptApplicationEntity entity) {
        this.firstConsent=entity.getFirstConsent();
        this.firstResponse=entity.getFirstResponse();
        this.secondResponse=entity.getSecondResponse();
        this.thirdResponse=entity.getFirstResponse();
        this.fourthResponse=entity.getFourthResponse();
        this.firstAdoptionResponse=entity.getFirstAdoptionResponse();
        this.secondAdoptionResponse=entity.getSecondAdoptionResponse();
    }

    public AdoptApplicationEntity toEntity() { return new AdoptApplicationEntity(this);}
}
