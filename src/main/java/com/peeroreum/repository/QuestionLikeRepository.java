package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.like.QuestionLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Long> {
    Optional<QuestionLike> findByQuestionAndMember(Question question, Member member);
    void deleteByQuestionAndMember(Question question, Member member);
}
