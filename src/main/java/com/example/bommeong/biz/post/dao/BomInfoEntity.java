package com.example.bommeong.biz.post.dao;

import com.example.bommeong.biz.post.dto.BomInfoModel;
import com.example.bommeong.biz.post.dto.PostModel;
import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

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
    private PostEntity postEntity;

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

    @Column(name = "extra")
    private String extra;

    public BomInfoEntity(BomInfoModel model) {}

    public BomInfoEntity(PostModel model) {
        this.name = model.getBomInfo().getName();
        this.age = model.getBomInfo().getAge();
        this.breed = model.getBomInfo().getBreed();
        this.gender = model.getBomInfo().getGender();
        this.personality = model.getBomInfo().getPersonality();
        this.extra = model.getBomInfo().getExtra();
    }

    @Override
    public BomInfoModel toModel() { return new BomInfoModel(this);}
}
