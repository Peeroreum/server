package com.example.demo.repository;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);
    List<Answer> findAllByMember(Member member);
    Long countByParentId(Long parentId);
    Long countByQuestionId(Long questionId);
}
