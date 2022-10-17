package com.example.demo.service;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.exception.ExceptionType.*;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final S3Service s3Service;

    @Transactional
    public void create(QuestionSaveDto saveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND_EXCEPTION));
        Question question = Question.builder()
                .content(saveDto.getContent())
                .member(member)
                .subject(saveDto.getSubject())
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

    @Transactional
    public void update(Long id, QuestionUpdateDto updateDto) {
//        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
//        List<ImageDto> existImages = imageService.findAllByQuestion(id); // 이미 저장된 이미지
//        List<MultipartFile> requestFiles = updateDto.getFiles(); // 업데이트 요청
//        List<MultipartFile> updateFiles = new ArrayList<>(); // 새로 저장할 파일
//
//        if(CollectionUtils.isEmpty(existImages)) {
//            if (!CollectionUtils.isEmpty(requestFiles)) {
//                for (MultipartFile requestFile : requestFiles)
//                    updateFiles.add(requestFile);
//            }
//        }
//        else {
//            if(CollectionUtils.isEmpty(requestFiles)) {
//                for(ImageDto image : existImages)
//                    imageService.deleteImage(image.getId());
//            }
//            else {
//                List<String> existImagesName = new ArrayList<>();
//
//                for(ImageDto existImage : existImages) {
//                    ImageDto image = imageService.findByImageId(existImage.getId());
//                    String imageName = image.getImageName();
//
//                    if(!requestFiles.contains(imageName))
//                        imageService.deleteImage(existImage.getId());
//                    else
//                        existImagesName.add(imageName);
//                }
//
//                for(MultipartFile multipartFile : requestFiles) {
//                    String multipartOriginalName = multipartFile.getOriginalFilename();
//                    if(!existImagesName.contains(multipartOriginalName))
//                        updateFiles.add(multipartFile);
//                }
//            }
//        }
//        List<Image> imageList = fileHandler.parseFileInfo(question, updateFiles);
//        if(!imageList.isEmpty()) {
//            for(Image image : imageList)
//                imageRepository.save(image);
//        }
//
//        question.update(updateDto.getContent(), updateDto.getSubject());
    }
    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(QUESTION_NOT_FOUND_EXCEPTION));
        List<Image> images = question.getImages();
        if(!images.isEmpty()) {
            for(Image image : images) {
                s3Service.deleteImage(image.getImageName());
            }
        }
        questionRepository.delete(question);
    }

    public List<QuestionListDto> search(QuestionSearchRequest searchRequest) {
        List<Question> searchedQuestions = questionRepository.findAllBySubjectAndMemberGrade(searchRequest.getSubject(), searchRequest.getGrade());
        List<QuestionListDto> results = new ArrayList<>();
        for(Question question : searchedQuestions) {
            results.add(new QuestionListDto(question, answerRepository.countByQuestionId(question.getId())));
        }
        return results;
    }
}
