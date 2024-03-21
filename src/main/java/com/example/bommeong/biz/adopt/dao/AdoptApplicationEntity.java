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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", unique = true, nullable = false)
    private PostEntity postEntity;

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

    @OneToOne(mappedBy = "adoptEntity", cascade = CascadeType.ALL)
    private AdoptApplicationEntity adoptApplicationEntity;

    public AdoptApplicationEntity(AdoptApplicationModel model) {}

    public AdoptApplicationEntity(AdoptModel model) {
        this.applicationId=model.getAdoptApplication().toEntity().getApplicationId();
        this.firstConsent=model.getAdoptApplication().toEntity().getFirstConsent();
        this.firstResponse=model.getAdoptApplication().toEntity().getFirstResponse();
        this.secondResponse=model.getAdoptApplication().toEntity().getSecondResponse();
        this.thirdResponse=model.getAdoptApplication().toEntity().getThirdResponse();
        this.fourthResponse=model.getAdoptApplication().toEntity().getFourthResponse();
        this.firstAdoptionResponse=model.getAdoptApplication().toEntity().getFirstAdoptionResponse();
        this.secondAdoptionResponse=model.getAdoptApplication().toEntity().getSecondAdoptionResponse();
    }

    public AdoptApplicationModel toModel() { return new AdoptApplicationModel(this);}

}
