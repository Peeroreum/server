package com.peeroreum.domain.image;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Wedu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class ChallengeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedu_id")
    private Wedu wedu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    private LocalDate challengeDate;

    @Builder
    public ChallengeImage(Member member, Wedu wedu, Image image, LocalDate challengeDate) {
        this.member = member;
        this.wedu = wedu;
        this.image = image;
        this.challengeDate = challengeDate;
    }
}
