package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Heart;
import com.example.demo.domain.Member;
import com.example.demo.domain.Question;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.HeartRepository;
import com.example.demo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.demo.exception.ExceptionType.*;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    public void likeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Member member = question.getMember();
        if(heartRepository.findLikeByMemberAndQuestionId(member, questionId).isPresent()) {
            throw new CustomException(ALREADY_LIKED);
        }

        Heart heart = new Heart(member, question);
        heartRepository.save(heart);
        question.updateLikes(1);
        questionRepository.save(question);
    }

    public void likeAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Member member = answer.getMember();
        if(heartRepository.findLikeByMemberAndAnswerId(member, answerId).isPresent())
            throw new CustomException(ALREADY_LIKED);

        Heart heart = new Heart(member, answer);
        heartRepository.save(heart);
        answer.updateLikes(1);
        answerRepository.save(answer);
    }
    public void unlikeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Member member = question.getMember();
        Optional<Heart> like = heartRepository.findLikeByMemberAndQuestionId(member, questionId);
        heartRepository.delete(like.get());
        question.updateLikes(-1);
        questionRepository.save(question);
    }



    public void unlikeAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Member member = answer.getMember();
        Optional<Heart> like = heartRepository.findLikeByMemberAndAnswerId(member, answerId);
        heartRepository.delete(like.get());
        answer.updateLikes(-1);
        answerRepository.save(answer);
    }
}
