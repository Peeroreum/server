package com.example.demo.repository;

import com.example.demo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Override
    Optional<Question> findById(Long id);

    List<Question> findAllBySubject(String subject);
    List<Question> findAllByMemberGrade(String grade);
}
