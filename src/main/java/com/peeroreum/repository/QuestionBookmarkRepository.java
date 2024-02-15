package com.peeroreum.repository;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.bookmark.QuestionBookMark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBookmarkRepository extends JpaRepository<QuestionBookMark, Long> {
    List<QuestionBookMark> findAllByMemberOrderByIdDesc(Member member, Pageable pageable);
    boolean existsByQuestionAndMember(Question question, Member member);
    void deleteAllByMember(Member member);
    void deleteAllByQuestion(Question question);
    void deleteByQuestionAndMember(Question question, Member member);
}
