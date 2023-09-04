package com.peeroreum.repository;

import com.peeroreum.domain.Heart;
import com.peeroreum.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findLikeByMemberAndQuestionId(Member member, Long questionId);
    Optional<Heart> findLikeByMemberAndAnswerId(Member member, Long answerId);
    boolean existsByMemberAndQuestionId(Member member, Long questionId);
    boolean existsByMemberAndAnswerId(Member member, Long answerId);
}
