package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class MemberWedu extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedu_id")
    private Wedu wedu;

    @Column
    private int continuousDate;

    @Builder
    public MemberWedu(Member member, Wedu wedu) {
        this.member = member;
        this.wedu = wedu;
        this.continuousDate = 0;
    }

    public void updateContinuousDate(int continuousDate) {
        this.continuousDate = continuousDate;
    }
}
