package com.example.bommeong.biz.adopt.dao;

import com.example.bommeong.biz.adopt.dto.AdoptModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "adopt")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class AdoptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_id")
    private Long adoptId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "status")
    private String status;

    @Transient
    private MultipartFile uploadFile;

    @OneToOne(mappedBy = "adoptEntity", cascade = CascadeType.ALL)
    private AdoptApplicationEntity AdoptApplicationEntity;

    public AdoptEntity(AdoptModel model) {
        this.adoptId = model.getAdoptId();
        this.imageName = model.getImageName();
        this.imageUrl = model.getImageUrl();
        this.status = model.getStatus();
        this.uploadFile = model.getUploadFile();
    }

    public AdoptModel toModel() { return new AdoptModel(this); }

}
