package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.bookmark.QuestionBookMark;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.repository.QuestionBookmarkRepository;
import com.peeroreum.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BookmarkService {

    private final QuestionBookmarkRepository questionBookmarkRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    public BookmarkService(QuestionBookmarkRepository questionBookmarkRepository, QuestionRepository questionRepository, MemberRepository memberRepository) {
        this.questionBookmarkRepository = questionBookmarkRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
    }

    public QuestionBookMark makeQuestionBookmark(Long id, String name) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(questionBookmarkRepository.existsByQuestionAndMember(question, member)) {
            throw new CustomException(ExceptionType.ALREADY_BOOKMARKED);
        }

        QuestionBookMark questionBookMark = new QuestionBookMark(member, question);
        return questionBookmarkRepository.save(questionBookMark);
    }

    public void cancelQuestionBookmark(Long id, String name) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(!questionBookmarkRepository.existsByQuestionAndMember(question, member)) {
            throw new CustomException(ExceptionType.BOOKMARK_NOT_FOUND);
        }

        questionBookmarkRepository.deleteByQuestionAndMember(question, member);
    }

    public void deleteAllByQuestion(Question question) {
        questionBookmarkRepository.deleteAllByQuestion(question);
    }

    public void deleteAllByMember(Member member) {
        questionBookmarkRepository.deleteAllByMember(member);
    }
}
