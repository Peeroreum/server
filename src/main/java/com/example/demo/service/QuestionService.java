package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Image;
import com.example.demo.domain.Question;
import com.example.demo.domain.Member;
import com.example.demo.dto.Attachment.ImageDto;
import com.example.demo.dto.question.*;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.attachment.ImageService;
import com.example.demo.service.attachment.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.exception.ExceptionType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final S3Service s3Service;

    public void create(QuestionSaveDto saveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND_EXCEPTION));
        Question question = Question.builder()
                .content(saveDto.getContent())
                .member(member)
                .subject(saveDto.getSubject())
                .grade(saveDto.getGrade())
                .build();
        List<Image> imageList = s3Service.uploadImage(saveDto.getFiles());
        if(!imageList.isEmpty()) {
            for(Image image : imageList)
                question.addImage(imageRepository.save(image));
        }
        questionRepository.save(question);
    }

    public QuestionReadDto read(Long id) {
        List<ImageDto> imageList = imageService.findAllByQuestion(id);
        List<String> imagePaths = new ArrayList<>();
        for(ImageDto image : imageList)
            imagePaths.add(image.getImagePath());
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        return new QuestionReadDto(question, imagePaths, answerRepository.countByQuestionId(id));
    }

    public void update(Long id, QuestionUpdateDto updateDto) {
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        List<Image> images = question.getImages(); //기존 저장 이미지 삭제
        if(!images.isEmpty()) {
            for(Image image : images) {
                s3Service.deleteImage(image.getImageName());
                imageRepository.delete(image);
            }
        }
        question.clearImage();

        List<Image> imageList = s3Service.uploadImage(updateDto.getFiles()); // 이미지 새로 저장
        if(!imageList.isEmpty()) {
            for(Image image : imageList)
                question.addImage(imageRepository.save(image));
        }
        question.update(updateDto.getContent(), updateDto.getSubject(), updateDto.getGrade());
        questionRepository.save(question);
    }

    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(QUESTION_NOT_FOUND_EXCEPTION));

        List<Image> images = question.getImages();
        if(!images.isEmpty()) {
            for(Image image : images) {
                s3Service.deleteImage(image.getImageName());
                imageRepository.delete(image);
            }
        }

        List<Answer> answers = answerRepository.findAllByQuestionId(id);
        if(!answers.isEmpty()) {
            answerRepository.deleteAll(answers);
        }

        questionRepository.delete(question);
    }

    public List<QuestionListDto> search(QuestionSearchRequest searchRequest) {
        List<Question> searchedQuestions;
        List<QuestionListDto> results = new ArrayList<>();
        if(searchRequest.getSubject() == 0 && searchRequest.getGrade() == 0) {
            searchedQuestions = questionRepository.findAll();
        }
        else {
            if(searchRequest.getSubject() == 0) {
                searchedQuestions = questionRepository.findAllByGrade(searchRequest.getGrade());
            }
            else if(searchRequest.getGrade() == 0) {
                searchedQuestions = questionRepository.findAllBySubject(searchRequest.getSubject());
            }
            else {
                searchedQuestions = questionRepository.findAllBySubjectAndGrade(searchRequest.getSubject(), searchRequest.getGrade());
            }
        }
        for(Question question : searchedQuestions) {
            results.add(new QuestionListDto(question, answerRepository.countByQuestionId(question.getId())));
        }
        return results;
    }
}
