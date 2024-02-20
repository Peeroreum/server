package com.peeroreum.repository;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.like.AnswerLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {
    Long countAllByAnswer(Answer answer);
    boolean existsByAnswerAndMember(Answer answer, Member member);
    void deleteByAnswerAndMember(Answer answer, Member member);
    void deleteAllByAnswer(Answer answer);
}
