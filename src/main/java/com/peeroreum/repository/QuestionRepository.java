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
    List<Question> findAllByTitleContainingOrContentContainingAndGradeOrderByIdDesc(String title, String content, Long grade, Pageable pageable);
    List<Question> findAllByTitleContainingOrContentContainingAndSubjectOrderByIdDesc(String title, String content, Long subject, Pageable pageable);
    List<Question> findAllByTitleContainingOrContentContainingAndSubjectAndGradeOrderByIdDesc(String title, String content, Long subject, Long grade, Pageable pageable);
    List<Question> findAllByTitleContainingOrContentContainingAndSubjectAndDetailSubjectOrderByIdDesc(String title, String content, Long subject, Long detailSubject, Pageable pageable);
    List<Question> findAllByTitleContainingOrContentContainingAndGradeAndSubjectAndDetailSubjectOrderByIdDesc(String title, String content, Long grade, Long subject, Long detailSubject, Pageable pageable);
}
