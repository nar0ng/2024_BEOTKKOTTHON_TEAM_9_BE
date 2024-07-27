package com.example.bommeong.biz.adopt.dao;

import com.example.bommeong.biz.adopt.dto.AdoptApplicationModel;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "adoptApplication")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class AdoptApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

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

    @Column(name = "dog_news")
    private AnswerType dogNews;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopt_id", referencedColumnName = "adopt_id")
    private AdoptEntity adoptEntity;

    public AdoptApplicationEntity(AdoptApplicationModel model) {}

    public AdoptApplicationEntity(AdoptModel model) {
        //        this.applicationId=model.getAdoptApplication().get();
        this.petHistoryAnswer = model.getAdoptApplication().getPetHistoryAnswer();
        this.petHistory = model.getAdoptApplication().getPetHistory();
        this.currentPetAnswer = model.getAdoptApplication().getCurrentPetAnswer();
        this.currentPet = model.getAdoptApplication().getCurrentPet();
        this.familyAnswer =  model.getAdoptApplication().getFamilyAnswer();
        this.familyAgreement = model.getAdoptApplication().getFamilyAgreement();
        this.reasonForAdoption = model.getAdoptApplication().getReasonForAdoption();
        this.dogNews = model.getAdoptApplication().getDogNews();
    }

    public AdoptApplicationModel toModel() { return new AdoptApplicationModel(this);}

}
