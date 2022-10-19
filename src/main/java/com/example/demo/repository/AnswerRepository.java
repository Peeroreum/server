package com.example.demo.repository;

import com.example.demo.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);
    List<Answer> findAllByMember(Long memberId);
    Long countByParentId(Long parentId);
    Long countByQuestionId(Long questionId);
    Long countByMember(Long memberId);
}
