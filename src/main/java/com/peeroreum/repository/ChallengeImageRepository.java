package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Wedu;
import com.peeroreum.domain.image.ChallengeImage;
import com.peeroreum.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChallengeImageRepository extends JpaRepository<ChallengeImage, Long> {
    ChallengeImage findAllByMemberAndWeduAndChallengeDate(Member member, Wedu wedu, LocalDate challengeDate);
}
