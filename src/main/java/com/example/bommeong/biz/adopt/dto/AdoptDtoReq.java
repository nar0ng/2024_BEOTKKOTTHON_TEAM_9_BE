package com.example.bommeong.biz.adopt.dto;

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

    @Column(name = "first_consent")
    private String firstConsent;

    @Transient
    private MultipartFile uploadFile;

    @Column(name = "first_response")
    private String firstResponse;

    @Column(name="second_response")
    private String secondResponse;

    @Column(name="third_response")
    private String thirdResponse;

    @Column(name="fourth_response")
    private String fourthResponse;

    @Column(name="first_adoption_response")
    private String firstAdoptionResponse;

    @Column(name="second_adoption_response")
    private String secondAdoptionResponse;

    @Column(name="third_adoption_response")
    private String thirdAdoptionResponse;

    @Column(name="fourth_adoption_response")
    private String fourthAdoptionResponse;
}
