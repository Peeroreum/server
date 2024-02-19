package com.peeroreum.domain.like;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.EntityTime;
import com.peeroreum.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AnswerLike extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    public AnswerLike(Member member, Answer answer) {
        this.member = member;
        this.answer = answer;
    }
}
