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

    @Column(name = "pet_history")
    private String petHistory;

    @Column(name = "current_pet")
    private String currentPet;

    @Column(name = "reason_for_adoption")
    private String reasonForAdoption;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adopt_id", referencedColumnName = "adopt_id")
    private AdoptEntity adoptEntity;

    public AdoptApplicationEntity(AdoptApplicationModel model) {}

    public AdoptApplicationEntity(AdoptModel model) {
        this.petHistory = model.getAdoptApplication().getPetHistory();
        this.reasonForAdoption = model.getAdoptApplication().getReasonForAdoption();
        this.currentPet = model.getAdoptApplication().getCurrentPet();
    }

    public AdoptApplicationModel toModel() { return new AdoptApplicationModel(this);}

}
