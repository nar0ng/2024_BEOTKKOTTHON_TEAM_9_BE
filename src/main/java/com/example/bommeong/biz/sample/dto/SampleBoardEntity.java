package com.example.bommeong.biz.sample.dto;


import com.example.bommeong.common.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


// @Data ==> @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
@Getter @Setter
@Entity
@Table(name="SAMPLE_BOARD")
@ToString
@NoArgsConstructor
public class SampleBoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_NO")
    private Long boardNo;

    @Column(name = "BOARD_NAME")
    private String boardName;

    @Column(name = "OWNER_ID")
    private String ownerId;

    public SampleBoardEntity(SampleBoardModel model) {
        this.boardNo = model.getBoardNo();
        this.boardName = model.getBoardName();
        this.ownerId = model.getOwnerId();
        this.createdAt = model.getCreatedAt();
        this.updatedAt = model.getUpdatedAt();
    }

    @Override
    public SampleBoardModel toModel() {
        return new SampleBoardModel(this);
    }


//    @ManyToOne(targetEntity = SampleMemberDto.class, fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn (name="USER_ID",  insertable=false, updatable=false)
//    @ManyToOne // (cascade = CascadeType.ALL)
    //private SampleMemberDto sampleMemberDto;


}
