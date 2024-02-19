package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByGradeAndSubjectAndDetailSubjectOrderByIdDesc(Long grade, Long subject, Long detailSubject, Pageable pageable);
    List<Question> findAllBySubjectAndGradeOrderByIdDesc(Long subject, Long grade, Pageable pageable);
    List<Question> findAllBySubjectAndDetailSubjectOrderByIdDes(Long subject, Long detailSubject, Pageable pageable);
    List<Question> findAllBySubjectOrderByIdDes(Long subject, Pageable pageable);
    List<Question> findAllByGradeOrderByIdDes(Long grade, Pageable pageable);
    List<Question> findAllByOrderByIdDes(Pageable pageable);

    List<Question> findAllByMemberOrderByIdDes(Member member, Pageable pageable);

    List<Question> findAllByTitleAndContentContainingOrderByIdDesc(String keyword, Pageable pageable);
}
