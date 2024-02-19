package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.like.QuestionLike;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.repository.QuestionLikeRepository;
import com.peeroreum.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LikeService {
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    public LikeService(QuestionLikeRepository questionLikeRepository, QuestionRepository questionRepository, MemberRepository memberRepository) {
        this.questionLikeRepository = questionLikeRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
    }

    public QuestionLike makeQuestionLike(Long id, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if(questionLikeRepository.existsByQuestionAndMember(question, member)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }

        QuestionLike questionLike = new QuestionLike(member, question);
        return questionLikeRepository.save(questionLike);
    }

    public void cancelQuestionLike(Long id, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if(!questionLikeRepository.existsByQuestionAndMember(question, member)) {
            throw new CustomException(ExceptionType.LIKE_NOT_FOUND);
        }

        questionLikeRepository.deleteByQuestionAndMember(question, member);
    }

    public Long countByQuestion(Question question) {
        return questionLikeRepository.countAllByQuestion(question);
    }
}
