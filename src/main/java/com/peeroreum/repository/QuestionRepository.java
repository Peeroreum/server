package com.peeroreum.repository;

import com.peeroreum.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySubjectAndGrade(Long subject, Long grade);
    List<Question> findAllBySubject(Long subject);
    List<Question> findAllByGrade(Long grade);
    Long countByMemberId(@Param("user_id") Long memberId);
}
