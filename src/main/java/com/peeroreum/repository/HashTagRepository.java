package com.peeroreum.repository;

import com.peeroreum.domain.HashTag;
import com.peeroreum.domain.Wedu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    void deleteAllByWedu(Wedu wedu);
    List<HashTag> getAllByWedu(Wedu wedu);
    List<HashTag> findAllByTagContaining(String keyword);
}
