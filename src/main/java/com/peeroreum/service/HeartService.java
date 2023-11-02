package com.peeroreum.service;

import com.peeroreum.domain.*;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void likeQuestion(Long questionId, String username) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(heartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }

        Heart heart = new Heart(member, question);
        heartRepository.save(heart);
        question.updateLikes(1);
        questionRepository.save(question);
    }

    public void likeAnswer(Long answerId, String username) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(heartRepository.existsByMemberAndAnswerId(member, answerId))
            throw new CustomException(ExceptionType.ALREADY_LIKED);

        Heart heart = new Heart(member, answer);
        heartRepository.save(heart);
        answer.updateLikes(1);
        answerRepository.save(answer);
    }

    public void cancelQuestionLike(Long questionId, String username) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        Heart like = heartRepository.findLikeByMemberAndQuestionId(member, questionId).orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_FOUND));
        heartRepository.delete(like);
        question.updateLikes(-1);
        questionRepository.save(question);
    }

    public void cancelAnswerLike(Long answerId, String username) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        Heart like = heartRepository.findLikeByMemberAndAnswerId(member, answerId).orElseThrow(() -> new CustomException(ExceptionType.LIKE_NOT_FOUND));
        heartRepository.delete(like);
        answer.updateLikes(-1);
        answerRepository.save(answer);
    }

}
