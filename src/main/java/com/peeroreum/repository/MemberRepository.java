package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    List<Member> findAll();

    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}