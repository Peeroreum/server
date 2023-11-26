package com.peeroreum.repository;

import com.peeroreum.domain.Wedu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeduRepository extends JpaRepository<Wedu, Long> {

    @Override
    List<Wedu> findAll();
    Wedu save(Wedu wedu);

    List<Wedu> findAllByGradeAndSubject(Long grade, Long subject);
    List<Wedu> findAllByGrade(Long grade);
    List<Wedu> findAllBySubject(Long subject);

}
