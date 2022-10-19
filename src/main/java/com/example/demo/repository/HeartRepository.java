package com.example.demo.repository;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Heart;
import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findLikeByMemberAndQuestionId(Member member, Long questionId);
    Optional<Heart> findLikeByMemberAndAnswerId(Member member, Long answerId);
    Long countByAnswer(Answer answer);
}
