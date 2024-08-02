package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.post.dto.BomInfoModel;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "bomInfo")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class BomInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long infoId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", unique = true, nullable = false)
    private PostEntity post;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private String age;

    @Column(name = "breed")
    private String breed;

    @Column(name = "gender")
    private String gender;

    @Column(name = "personality")
    private String personality;

    @Column(name = "likes")
    private String likes;

    @Column(name = "hates")
    private String hates;

    @Column(name = "extra")
    private String extra;

    @Column(name = "hashtags")
    private String hashtags;

    public BomInfoEntity(BomInfoModel model) {}

    public BomInfoEntity(PostModel model) {
        this.name = model.getBomInfo().getName();
        this.age = model.getBomInfo().getAge();
        this.breed = model.getBomInfo().getBreed();
        this.gender = model.getBomInfo().getGender();
        this.personality = model.getBomInfo().getPersonality();
        this.likes = model.getBomInfo().getLikes();
        this.hates = model.getBomInfo().getHates();
        this.extra = model.getBomInfo().getExtra();
        this.hashtags = model.getBomInfo().getHashtags();
    }

    @Override
    public BomInfoModel toModel() { return new BomInfoModel(this);}

}
