package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Wedu;
import com.peeroreum.domain.image.ChallengeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChallengeImageRepository extends JpaRepository<ChallengeImage, Long> {
    ChallengeImage findAllByMemberAndWeduAndChallengeDate(Member member, Wedu wedu, LocalDate challengeDate);
    List<ChallengeImage> findAllByWedu(Wedu wedu);
    List<ChallengeImage> findAllByWeduAndMember(Wedu wedu, Member member);
    List<ChallengeImage> findAllByMemberAndWeduOrderByChallengeDateDesc(Member member, Wedu wedu);
    List<ChallengeImage> findAllByWeduAndChallengeDate(Wedu wedu, LocalDate challengeDate);
    Long countAllByWeduAndChallengeDate(Wedu wedu, LocalDate challengeDate);
    void deleteByWeduAndMemberAndChallengeDate(Wedu wedu, Member member, LocalDate challengeDate);
    void deleteAllByWeduAndMember(Wedu wedu, Member member);
    void deleteAllByWedu(Wedu wedu);
    void deleteAllByMemberAndWeduAndChallengeDate(Member member, Wedu wedu, LocalDate now);
    boolean existsByWeduAndMemberAndChallengeDate(Wedu wedu, Member member, LocalDate challengeDate);
}
