package com.example.demo.repository;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Member;
import com.example.demo.domain.XHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface XHeartRepository extends JpaRepository<XHeart, Long> {
    Optional<XHeart> findByMemberAndQuestionId(Member member, Long questionId);
    Optional<XHeart> findByMemberAndAnswerId(Member member, Long answerId);
    Long countByAnswer(Answer answer);
}
