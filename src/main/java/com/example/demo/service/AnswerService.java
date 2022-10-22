package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Image;
import com.example.demo.domain.Member;
import com.example.demo.domain.Question;
import com.example.demo.dto.Attachment.ImageDto;
import com.example.demo.dto.answer.*;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.*;
import com.example.demo.service.attachment.ImageService;
import com.example.demo.service.attachment.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
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
    private final HeartRepository heartRepository;
    private final XHeartRepository xHeartRepository;
    private final ImageService imageService;
    private final S3Service s3Service;

    public void create(AnswerSaveDto saveDto, String username) {
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
        if(!CollectionUtils.isEmpty(saveDto.getFiles())) {
            List<Image> imageList = s3Service.uploadImage(saveDto.getFiles());
            for(Image image : imageList)
                answer.addImage(imageRepository.save(image));
        }
        questionRepository.save(question);
        answerRepository.save(answer);
    }

    public List<AnswerReadDto> readAll(AnswerReadRequest readRequest, String username) {
        List<Answer> answers = answerRepository.findAllByQuestionId(readRequest.getQuestionId());
        List<AnswerReadDto> result = new ArrayList<>();
        for(Answer answer : answers) {
            boolean liked = heartRepository.existsByMemberAndAnswerId(memberRepository.findByUsername(username).get(), answer.getId());
            boolean disliked = xHeartRepository.existsByMemberAndAnswerId(memberRepository.findByUsername(username).get(), answer.getId());
            List<ImageDto> imageDtoList = imageService.findAllByAnswer(answer.getId());
            List<String> imagePaths = new ArrayList<>();
            for(ImageDto image : imageDtoList)
                imagePaths.add(image.getImagePath());
            result.add(new AnswerReadDto(username, liked, disliked, answer, imagePaths));
        }
        return result;
    }


    public void update(Long id, AnswerUpdateDto answerUpdateDto) {
        Answer answer = answerRepository.findById(id).orElseThrow(()->new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        List<Image> images = answer.getImages(); //기존 저장 이미지 삭제
        if(!images.isEmpty()) {
            for(Image image : images) {
                s3Service.deleteImage(image.getImageName());
                imageRepository.delete(image);
            }
        }
        answer.clearImage();

        if(!CollectionUtils.isEmpty(answerUpdateDto.getFiles())) {
            List<Image> imageList = s3Service.uploadImage(answerUpdateDto.getFiles()); // 이미지 새로 저장
            for(Image image : imageList)
                answer.addImage(imageRepository.save(image));
        }
        answer.update(answerUpdateDto.getContent());
        answerRepository.save(answer);
    }

    public void delete(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(()->new CustomException(ANSWER_NOT_FOUND_EXCEPTION));
        Optional<Answer> parent = Optional.ofNullable(answer.getParent());
        Long parentId;
        List<Image> images = answer.getImages();
        if(parent.isPresent())
            parentId = answer.getParent().getId();
        else parentId = 0L;

        if(parentId == 0 && answerRepository.countByParentId(answer.getId()) > 0) {
            answer.delete();
            answerRepository.save(answer);
        }
        else {
            answerRepository.delete(answer);
        }
        if(!images.isEmpty()) {
            for(Image image : images) {
                s3Service.deleteImage(image.getImageName());
            }
        }
    }

}
