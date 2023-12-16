package com.peeroreum.repository;

import com.peeroreum.domain.HashTag;
import com.peeroreum.domain.Wedu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WeduRepository extends JpaRepository<Wedu, Long> {

    @Override
    List<Wedu> findAll();
    Wedu save(Wedu wedu);

    List<Wedu> findAllByGradeAndSubject(Long grade, Long subject);
    List<Wedu> findAllByGrade(Long grade);
    List<Wedu> findAllBySubject(Long subject);

//    @Query("SELECT w FROM Wedu w LEFT JOIN w.hashTags h WHERE LOWER(w.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(h.tag) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Wedu> findAllByTitleContaining(String title);
}
