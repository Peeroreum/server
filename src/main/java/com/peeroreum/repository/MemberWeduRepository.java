package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.MemberWedu;
import com.peeroreum.domain.Wedu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Repository
public interface MemberWeduRepository extends JpaRepository<MemberWedu, Long> {
    List<MemberWedu> findAllByMember(Member member);
    MemberWedu findByMemberAndWedu(Member member, Wedu wedu);
    List<MemberWedu> findAllByWedu(Wedu wedu);
    List<MemberWedu> findAllByWeduAndCreatedTimeBefore(Wedu wedu, LocalDateTime localDateTime);
    void deleteAllByWedu(Wedu wedu);
    void deleteByWeduAndMember(Wedu wedu, Member member);
    int countAllByWedu(Wedu wedu);

    int countAllByWeduAndCreatedTimeBefore(Wedu wedu, LocalDateTime createdTime);
    boolean existsByMemberAndWedu(Member member, Wedu wedu);
}
