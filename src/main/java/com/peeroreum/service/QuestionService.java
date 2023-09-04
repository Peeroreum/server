package com.peeroreum.service;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Image;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.Member;
import com.peeroreum.dto.Attachment.ImageDto;
import com.peeroreum.dto.question.*;
import com.peeroreum.exception.CustomException;
import com.peeroreum.service.attachment.ImageService;
import com.peeroreum.service.attachment.S3Service;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final ImageRepository imageRepository;
    private final HeartRepository heartRepository;
    private final XHeartRepository xHeartRepository;
    private final ImageService imageService;
    private final S3Service s3Service;

    public void create(QuestionSaveDto saveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = Question.builder()
                .content(saveDto.getContent())
                .member(member)
                .subject(saveDto.getSubject())
                .grade(saveDto.getGrade())
                .build();

        if(!CollectionUtils.isEmpty(saveDto.getFiles())) {
            List<Image> imageList = s3Service.uploadImage(saveDto.getFiles());
            for(Image image : imageList)
                question.addImage(imageRepository.save(image));
        }
        questionRepository.save(question);
    }

    public QuestionReadDto read(Long id, String username) {
        List<ImageDto> imageList = imageService.findAllByQuestion(id);
        List<String> imagePaths = new ArrayList<>();
        boolean liked = heartRepository.existsByMemberAndQuestionId(memberRepository.findByUsername(username).get(), id);
        boolean disliked = xHeartRepository.existsByMemberAndQuestionId(memberRepository.findByUsername(username).get(), id);
        for(ImageDto image : imageList)
            imagePaths.add(image.getImagePath());
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        return new QuestionReadDto(username, liked, disliked, question, imagePaths, answerRepository.countByQuestionId(id));
    }

    public void update(Long id, QuestionUpdateDto updateDto) {
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        List<Image> images = question.getImages(); //기존 저장 이미지 삭제
        if(!images.isEmpty()) {
            for(Image image : images) {
                s3Service.deleteImage(image.getImageName());
                imageRepository.delete(image);
            }
        }
        question.clearImage();

        if(!CollectionUtils.isEmpty(updateDto.getFiles())) {
            List<Image> imageList = s3Service.uploadImage(updateDto.getFiles()); // 이미지 새로 저장
            for(Image image : imageList)
                question.addImage(imageRepository.save(image));
        }
        question.update(updateDto.getContent(), updateDto.getSubject(), updateDto.getGrade());
        questionRepository.save(question);
    }

    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

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
        Collections.reverse(searchedQuestions);

        for(Question question : searchedQuestions) {
            results.add(new QuestionListDto(question, answerRepository.countByQuestionId(question.getId())));
        }
        return results;
    }
}
