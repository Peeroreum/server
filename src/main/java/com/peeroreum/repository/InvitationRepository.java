package com.peeroreum.repository;

import com.peeroreum.domain.Invitation;
import com.peeroreum.domain.Wedu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByWedu(Wedu wedu);
    void deleteByWedu(Wedu wedu);
}
