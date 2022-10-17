package com.example.demo.repository;

import com.example.demo.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Override
    Optional<Answer> findById(Long id);
    Long countByQuestionId(Long questionId);
    List<Answer> findAllByQuestionId(Long questionId);
    Long countByParentId(Long parentId);

}
