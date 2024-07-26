package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
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
    private String petHistory;
    private String reasonForAdoption;
    private String currentPet;

    public AdoptApplicationModel(AdoptApplicationEntity entity) {
        this.petHistory=entity.getPetHistory();
        this.reasonForAdoption = entity.getReasonForAdoption();
        this.currentPet = entity.getCurrentPet();
    }

    public AdoptApplicationEntity toEntity() { return new AdoptApplicationEntity(this);}
}
