package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Image;
import com.example.demo.domain.Member;
import com.example.demo.domain.Question;
import com.example.demo.dto.Attachment.ImageDto;
import com.example.demo.dto.answer.*;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.attachment.FileHandler;
import com.example.demo.service.attachment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.exception.ExceptionType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final FileHandler fileHandler;

    public void create(AnswerSaveDto saveDto, String username) throws IOException {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(saveDto.getQuestionId()).orElseThrow(()->new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        Answer parent = Optional.ofNullable(saveDto.getParentId()).map(id -> answerRepository.findById(id)
                .orElseThrow(()->new CustomException(ANSWER_NOT_FOUND_EXCEPTION))).orElse(null);
        Answer answer = Answer.builder()
                .content(saveDto.getContent())
                .member(member)
                .question(question)
                .parent(parent)
                .build();
        List<Image> imageList = fileHandler.parseFileInfo(answer, saveDto.getFiles());
        if(!imageList.isEmpty()) {
            for(Image image : imageList)
                answer.addImage(imageRepository.save(image));
        }
        question.addAnswer(answer);
        questionRepository.save(question);
        answerRepository.save(answer);
    }

    public List<AnswerReadDto> readAll(AnswerReadRequest readRequest) {
        List<Answer> answers = questionRepository.findById(readRequest.getQuestionId()).get().getAnswers();
        List<AnswerReadDto> result = new ArrayList<>();
        for(Answer answer : answers) {
            List<ImageDto> imageDtoList = imageService.findAllByAnswer(answer.getId());
            List<String> imageUris = new ArrayList<>();
            for(ImageDto image : imageDtoList)
                imageUris.add(image.getImagePath());
            result.add(new AnswerReadDto(answer, imageUris));
        }
        return result;
    }

    public void update(Long id, AnswerUpdateDto updateDto) {

    }

    public void delete(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(()->new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Optional<Answer> parent = Optional.ofNullable(answer.getParent());
        Long parentId;
        if(parent.isPresent())
            parentId = answer.getParent().getId();
        else parentId = 0L;

        answer.delete();
        questionRepository.save(answer.getQuestion());

        if(parentId == 0 && answerRepository.countByParentId(answer.getId()) > 0) {
        }
        else
            answerRepository.delete(answer);
    }
}
