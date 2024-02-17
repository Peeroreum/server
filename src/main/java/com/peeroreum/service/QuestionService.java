package com.peeroreum.service;

import com.peeroreum.domain.image.Image;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.Member;
import com.peeroreum.dto.question.*;
import com.peeroreum.exception.CustomException;
import com.peeroreum.service.attachment.ImageService;
import com.peeroreum.service.attachment.S3Service;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final S3Service s3Service;

    public Question create(QuestionSaveDto saveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = Question.builder()
                .title(saveDto.getTitle())
                .content(saveDto.getContent())
                .subject(saveDto.getSubject())
                .detailSubject(saveDto.getDetailSubject())
                .grade(member.getGrade())
                .member(member)
                .build();

        List<Image> images = new ArrayList<>();
        if(!saveDto.getFiles().isEmpty()) {
            for(MultipartFile file : saveDto.getFiles()) {
                images.add(s3Service.uploadImage(file));
            }
        }
        question.updateImages(images);

        return questionRepository.save(question);
    }

    public Question update(Long id, QuestionUpdateDto updateDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if(question.getMember() != member) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        if(updateDto.getContent() != null) {
            question.updateContent(updateDto.getContent());
        }

        if(updateDto.getFiles() != null) {
            if(!question.getImages().isEmpty()) {
                for(Image image : question.getImages()) {
                    imageService.deleteImage(image.getId());
                }
            }

            List<Image> images = new ArrayList<>();
            for(MultipartFile file : updateDto.getFiles()) {
                images.add(imageService.saveImage(file));
            }
            question.updateImages(images);
        }

        return questionRepository.save(question);
    }

    public void delete(Long id, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if(question.getMember() != member) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        if(!question.getImages().isEmpty()) {
            for(Image image : question.getImages()) {
                imageService.deleteImage(image.getId());
            }
        }

        questionRepository.delete(question);
    }
}
