package com.peeroreum.repository;

import com.peeroreum.domain.HashTag;
import com.peeroreum.domain.Wedu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WeduRepository extends JpaRepository<Wedu, Long> {

    List<Wedu> findAllByOrderByIdDesc(Pageable pageable);
    Wedu save(Wedu wedu);

    List<Wedu> findAllByGradeAndSubjectOrderByIdDesc(Long grade, Long subject, Pageable pageable);
    List<Wedu> findAllByGradeOrderByIdDesc(Long grade, Pageable pageable);
    List<Wedu> findAllBySubjectOrderByIdDesc(Long subject, Pageable pageable);

    List<Wedu> findAllByTitleContaining(String title);
}
