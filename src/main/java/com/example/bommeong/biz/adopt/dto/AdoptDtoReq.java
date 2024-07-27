package com.example.bommeong.biz.adopt.dto;

import com.example.bommeong.biz.adopt.dao.AnswerType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdoptDtoReq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "pet_history_answer")
    private AnswerType petHistoryAnswer;

    @Column(name = "pet_history")
    private String petHistory;

    @Column(name = "current_pet_answer")
    private AnswerType currentPetAnswer;

    @Column(name = "current_pet")
    private String currentPet;

    @Column(name = "family_answer")
    private AnswerType familyAnswer;

    @Column(name = "family_agreement")
    private AnswerType familyAgreement;

    @Column(name = "reason_for_adoption")
    private String reasonForAdoption;

    @Column(name = "dog_news_answer")
    private AnswerType dogNewsAnswer;
}
