package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.HeartRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.XHeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.demo.exception.ExceptionType.*;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final XHeartRepository xHeartRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    public void likeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Member member = question.getMember();
        if(heartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ALREADY_LIKED);
        }
        if(xHeartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ALREADY_DISLIKED);
        }

        Heart heart = new Heart(member, question);
        heartRepository.save(heart);
        question.updateLikes(1);
        questionRepository.save(question);
    }
    public void dislikeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Member member = question.getMember();
        if(xHeartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ALREADY_DISLIKED);
        }
        if(heartRepository.existsByMemberAndQuestionId(member, questionId)) {
            throw new CustomException(ALREADY_LIKED);
        }

        XHeart xHeart = new XHeart(member, question);
        xHeartRepository.save(xHeart);
        question.updateDislikes(1);
        questionRepository.save(question);
    }

    public void likeAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Member member = answer.getMember();
        if(heartRepository.existsByMemberAndAnswerId(member, answerId))
            throw new CustomException(ALREADY_LIKED);
        if(xHeartRepository.existsByMemberAndAnswerId(member, answerId)) {
            throw new CustomException(ALREADY_DISLIKED);
        }

        Heart heart = new Heart(member, answer);
        heartRepository.save(heart);
        answer.updateLikes(1);
        answerRepository.save(answer);
    }

    public void dislikeAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Member member = answer.getMember();
        if(xHeartRepository.existsByMemberAndAnswerId(member, answerId)) {
            throw new CustomException(ALREADY_DISLIKED);
        }
        if(heartRepository.existsByMemberAndAnswerId(member, answerId)) {
            throw new CustomException(ALREADY_LIKED);
        }

        XHeart xHeart = new XHeart(member, answer);
        xHeartRepository.save(xHeart);
        answer.updateDislikes(1);
        answerRepository.save(answer);
    }

    public void cancelQuestionLike(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Member member = question.getMember();
        Optional<Heart> like = heartRepository.findLikeByMemberAndQuestionId(member, questionId);
        heartRepository.delete(like.get());
        question.updateLikes(-1);
        questionRepository.save(question);
    }

    public void cancelQuestionDislike(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Member member = question.getMember();
        Optional<XHeart> dislike = xHeartRepository.findByMemberAndQuestionId(member, questionId);
        xHeartRepository.delete(dislike.get());
        question.updateDislikes(-1);
        questionRepository.save(question);
    }



    public void cancelAnswerLike(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Member member = answer.getMember();
        Optional<Heart> like = heartRepository.findLikeByMemberAndAnswerId(member, answerId);
        heartRepository.delete(like.get());
        answer.updateLikes(-1);
        answerRepository.save(answer);
    }

    public void cancelAnsweDislike(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Member member = answer.getMember();
        Optional<XHeart> dislike = xHeartRepository.findByMemberAndAnswerId(member, answerId);
        xHeartRepository.delete(dislike.get());
        answer.updateDislikes(-1);
        answerRepository.save(answer);
    }
}
