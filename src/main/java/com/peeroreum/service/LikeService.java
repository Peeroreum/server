package com.peeroreum.service;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.like.AnswerLike;
import com.peeroreum.domain.like.QuestionLike;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LikeService {
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionRepository questionRepository;
    private final AnswerLikeRepository answerLikeRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public LikeService(QuestionLikeRepository questionLikeRepository, QuestionRepository questionRepository, AnswerLikeRepository answerLikeRepository, AnswerRepository answerRepository, MemberRepository memberRepository) {
        this.questionLikeRepository = questionLikeRepository;
        this.questionRepository = questionRepository;
        this.answerLikeRepository = answerLikeRepository;
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
    }

    public QuestionLike makeQuestionLike(Long id, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if(isLikedQuestion(question, member)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }

        QuestionLike questionLike = new QuestionLike(member, question);
        return questionLikeRepository.save(questionLike);
    }

    public void cancelQuestionLike(Long id, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if(!isLikedQuestion(question, member)) {
            throw new CustomException(ExceptionType.LIKE_NOT_FOUND);
        }

        questionLikeRepository.deleteByQuestionAndMember(question, member);
    }

    public boolean isLikedQuestion(Question question, Member member) {
        return questionLikeRepository.existsByQuestionAndMember(question, member);
    }

    public Long countByQuestion(Question question) {
        return questionLikeRepository.countAllByQuestion(question);
    }

    public void deleteAllByQuestion(Question question) {
        questionLikeRepository.deleteAllByQuestion(question);
    }

    public AnswerLike makeAnswerLike(Long id, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));

        if(isLikedAnswer(answer, member)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }

        AnswerLike answerLike = new AnswerLike(member, answer);
        return answerLikeRepository.save(answerLike);
    }

    public void cancelAnswerLike(Long id, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));

        if(!isLikedAnswer(answer, member)) {
            throw new CustomException(ExceptionType.LIKE_NOT_FOUND);
        }

        answerLikeRepository.deleteByAnswerAndMember(answer, member);
    }

    public boolean isLikedAnswer(Answer answer, Member member) {
        return answerLikeRepository.existsByAnswerAndMember(answer, member);
    }

    public Long countByAnswer(Answer answer) {
        return answerLikeRepository.countAllByAnswer(answer);
    }
}
