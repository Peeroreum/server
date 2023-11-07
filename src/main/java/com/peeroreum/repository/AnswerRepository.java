package com.peeroreum.repository;

import com.peeroreum.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);
    List<Answer> findAllByMemberId(@Param("user_id") Long memberId);
    Long countByQuestionId(Long questionId);
}
