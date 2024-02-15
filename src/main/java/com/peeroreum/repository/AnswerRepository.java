package com.peeroreum.repository;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionIdOrderByGroupIdAscIdAsc(Long questionId, Pageable pageable);
    List<Answer> findAllByMember(Member member, Pageable pageable);
    Long countAllByQuestion(Question question);
}
