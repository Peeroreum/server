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
    private final XHeartRepository xHeartRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    public void likeQuestion(Long questionId, String username) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(heartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }
        if(xHeartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ExceptionType.ALREADY_DISLIKED);
        }

        Heart heart = new Heart(member, question);
        heartRepository.save(heart);
        question.updateLikes(1);
        questionRepository.save(question);
    }
    public void dislikeQuestion(Long questionId, String username) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        if(xHeartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ExceptionType.ALREADY_DISLIKED);
        }
        if(heartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }

        XHeart xHeart = new XHeart(member, question);
        xHeartRepository.save(xHeart);
        question.updateDislikes(1);
        questionRepository.save(question);
    }

    public void likeAnswer(Long answerId, String username) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(heartRepository.existsByMemberAndAnswerId(member, answerId))
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        if(xHeartRepository.existsByMemberAndAnswerId(member, answerId)) {
            throw new CustomException(ExceptionType.ALREADY_DISLIKED);
        }

        Heart heart = new Heart(member, answer);
        heartRepository.save(heart);
        answer.updateLikes(1);
        answerRepository.save(answer);
    }

    public void dislikeAnswer(Long answerId, String username) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(xHeartRepository.existsByMemberAndAnswerId(member, answerId)) {
            throw new CustomException(ExceptionType.ALREADY_DISLIKED);
        }
        if(heartRepository.existsByMemberAndAnswerId(member, answerId)) {
            throw new CustomException(ExceptionType.ALREADY_LIKED);
        }

        XHeart xHeart = new XHeart(member, answer);
        xHeartRepository.save(xHeart);
        answer.updateDislikes(1);
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

    public void cancelQuestionDislike(Long questionId, String username) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        XHeart dislike = xHeartRepository.findByMemberAndQuestionId(member, questionId).orElseThrow(() -> new CustomException(ExceptionType.DISLIKE_NOT_FOUND));
        xHeartRepository.delete(dislike);
        question.updateDislikes(-1);
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

    public void cancelAnswerDislike(Long answerId, String username) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        XHeart dislike = xHeartRepository.findByMemberAndAnswerId(member, answerId).orElseThrow(() -> new CustomException(ExceptionType.DISLIKE_NOT_FOUND));
        xHeartRepository.delete(dislike);
        answer.updateDislikes(-1);
        answerRepository.save(answer);
    }
}
