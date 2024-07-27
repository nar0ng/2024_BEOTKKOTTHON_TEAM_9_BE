package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import com.example.bommeong.biz.adopt.dao.AnswerType;
import java.util.Optional;
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
    private AnswerType petHistoryAnswer;
    private String petHistory;
    private AnswerType currentPetAnswer;
    private String currentPet;
    private AnswerType familyAnswer;
    private AnswerType familyAgreement;
    private String reasonForAdoption;
    private AnswerType dogNews;

    public AdoptApplicationModel(AdoptApplicationEntity entity) {
        this.petHistoryAnswer = entity.getPetHistoryAnswer();
        this.petHistory=entity.getPetHistory();
        this.currentPetAnswer = entity.getCurrentPetAnswer();
        this.currentPet = entity.getCurrentPet();
        this.familyAnswer = entity.getFamilyAnswer();
        this.familyAgreement = entity.getFamilyAgreement();
        this.reasonForAdoption = entity.getReasonForAdoption();
        this.dogNews = entity.getDogNews();
    }

    public AdoptApplicationEntity toEntity() { return new AdoptApplicationEntity(this);}
}
