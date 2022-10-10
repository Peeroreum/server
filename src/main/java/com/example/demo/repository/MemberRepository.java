package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByRefreshToken(String refreshToken);

    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
