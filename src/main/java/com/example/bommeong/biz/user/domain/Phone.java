package com.example.bommeong.biz.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "phone")
@Builder
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    private String cer;

    public static Phone createPhone(String number, String cer) {
        final Phone phone = Phone.builder()
                .number(number)
                .cer(cer)
                .build();
        return phone;
    }

    public static Phone updatePhone(Phone phone, String cer) {
        final Phone ph = Phone.builder()
                .number(phone.getNumber())
                .cer(cer)
                .build();
        return ph;
    }


}
