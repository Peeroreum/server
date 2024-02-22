package com.peeroreum.repository;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionOrderByGroupIdAscIdAsc(Question question, Pageable pageable);
    List<Answer> findAllByMember(Member member, Pageable pageable);
    List<Answer> findAllByQuestion(Question question);
    boolean existsByQuestionAndIsSelected(Question question, boolean isSelected);
    boolean existsByParentAnswerId(Long parentAnswerId);
    Long countAllByQuestion(Question question);
    Long countAllByParentAnswerId(Long parentAnswerId);
}
