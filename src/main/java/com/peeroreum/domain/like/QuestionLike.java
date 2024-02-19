package com.peeroreum.domain.like;

import com.peeroreum.domain.EntityTime;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class QuestionLike extends EntityTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public QuestionLike(Member member, Question question) {
        this.member = member;
        this.question = question;
    }
}

