package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.MemberWedu;
import com.peeroreum.domain.Wedu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberWeduRepository extends JpaRepository<MemberWedu, Long> {
    List<MemberWedu> findAllByMember(Member member);
    void deleteAllByWedu(Wedu wedu);
    int countAllByWedu(Wedu wedu);
    boolean existsByMemberAndWedu(Member member, Wedu wedu);
}
