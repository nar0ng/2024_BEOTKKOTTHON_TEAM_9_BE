package com.example.bommeong.biz.adopt.dao;

import com.example.bommeong.biz.adopt.dto.AdoptApplicationModel;
import com.example.bommeong.biz.adopt.dto.AdoptModel;
import com.example.bommeong.biz.post.dao.PostEntity;
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



    @Column(name = "first_consent")
    private String firstConsent;

    @Column(name = "first_response")
    private String firstResponse;

    @Column(name = "second_response")
    private String secondResponse;

    @Column(name = "third_response")
    private String thirdResponse;

    @Column(name = "fourth_response")
    private String fourthResponse;

    @Column(name = "first_adoption_response")
    private String firstAdoptionResponse;

    @Column(name = "second_adoption_response")
    private String secondAdoptionResponse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopt_id", referencedColumnName = "adopt_id")
    private AdoptEntity adoptEntity;

    public AdoptApplicationEntity(AdoptApplicationModel model) {}

    public AdoptApplicationEntity(AdoptModel model) {
//        this.applicationId=model.getAdoptApplication().get();
        this.firstConsent=model.getAdoptApplication().getFirstConsent();
        this.firstResponse=model.getAdoptApplication().getFirstResponse();
        this.secondResponse=model.getAdoptApplication().getSecondResponse();
        this.thirdResponse=model.getAdoptApplication().getThirdResponse();
        this.fourthResponse=model.getAdoptApplication().getFourthResponse();
        this.firstAdoptionResponse=model.getAdoptApplication().getFirstAdoptionResponse();
        this.secondAdoptionResponse=model.getAdoptApplication().getSecondAdoptionResponse();
    }

    public AdoptApplicationModel toModel() { return new AdoptApplicationModel(this);}

}
