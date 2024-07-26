package com.example.bommeong.biz.adopt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record AdoptDtoReq (
        Long id,
        MultipartFile uploadFile,
        String petHistory,
        String reasonForAdoption,
        String currentPet

){

}
