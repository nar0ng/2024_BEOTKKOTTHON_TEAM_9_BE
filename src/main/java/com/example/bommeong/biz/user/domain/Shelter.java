package com.example.bommeong.biz.user.domain;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import com.example.bommeong.biz.post.dao.LikeEntity;
import com.example.bommeong.biz.post.dao.PostEntity;
import com.example.bommeong.biz.user.dto.UserDtoReq;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "shelter")
@Builder
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shelter_name", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="manager_name", nullable = false)
    private String managerName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "sub_email")
    private String subEmail;

    @Column(name = "doc_name")
    private String docName;

    @Column(name = "doc_url")
    private String docUrl;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address")
    private String address;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAT;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAT;


//    @OneToMany(mappedBy = "shelter")
//    private List<PostEntity> posts = new ArrayList<>();
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private AdoptEntity adoptEntity;




}
