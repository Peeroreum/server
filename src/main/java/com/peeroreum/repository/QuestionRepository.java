package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    int countAllByMember(Member member);
    List<Question> findAllByGradeAndSubjectAndDetailSubjectOrderByIdDesc(Long grade, Long subject, Long detailSubject, Pageable pageable);
    List<Question> findAllBySubjectAndGradeOrderByIdDesc(Long subject, Long grade, Pageable pageable);
    List<Question> findAllBySubjectAndDetailSubjectOrderByIdDesc(Long subject, Long detailSubject, Pageable pageable);
    List<Question> findAllBySubjectOrderByIdDesc(Long subject, Pageable pageable);
    List<Question> findAllByGradeOrderByIdDesc(Long grade, Pageable pageable);
    List<Question> findAllByOrderByIdDesc(Pageable pageable);

    List<Question> findAllByMemberOrderByIdDesc(Member member, Pageable pageable);

    List<Question> findAllByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    List<Question> findAllByTitleContainingOrContentContainingOrderByIdDesc(String title, String content, Pageable pageable);
    List<Question> findAllByGradeAndTitleContainingOrGradeAndContentContainingOrderByIdDesc(Long grade1, String title, Long grade2, String content, Pageable pageable);
    List<Question> findAllBySubjectAndTitleContainingOrSubjectAndContentContainingOrderByIdDesc(Long subject1, String title, Long subject2, String content, Pageable pageable);
    List<Question> findAllBySubjectAndGradeAndTitleContainingOrSubjectAndGradeAndContentContainingOrderByIdDesc(Long subject1, Long grade1, String title, Long subject2, Long grade2, String content, Pageable pageable);
    List<Question> findAllBySubjectAndDetailSubjectAndTitleContainingOrSubjectAndDetailSubjectAndContentContainingOrderByIdDesc(Long subject1, Long detailSubject1, String title, Long subject2, Long detailSubject2, String content, Pageable pageable);
    List<Question> findAllByGradeAndSubjectAndDetailSubjectAndTitleContainingOrGradeAndSubjectAndDetailSubjectAndContentContainingOrderByIdDesc(Long grade1, Long subject1, Long detailSubject1, String title, Long grade2, Long subject2, Long detailSubject2, String content, Pageable pageable);
}
