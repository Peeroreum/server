package com.example.demo.repository;

import com.example.demo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySubjectAndMemberGrade(Long subject, Long grade);
    Long countByMemberId(@Param("user_id") Long memberId);
}
