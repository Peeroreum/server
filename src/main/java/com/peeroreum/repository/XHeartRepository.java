package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.XHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface XHeartRepository extends JpaRepository<XHeart, Long> {
    Optional<XHeart> findByMemberAndQuestionId(Member member, Long questionId);
    Optional<XHeart> findByMemberAndAnswerId(Member member, Long answerId);
    boolean existsByMemberAndQuestionId(Member member, Long questionId);
    boolean existsByMemberAndAnswerId(Member member, Long answerId);
}
