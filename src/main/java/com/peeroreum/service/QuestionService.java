package com.peeroreum.service;

import com.peeroreum.domain.image.Image;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.Member;
import com.peeroreum.dto.member.MemberProfileDto;
import com.peeroreum.dto.question.*;
import com.peeroreum.exception.CustomException;
import com.peeroreum.service.attachment.ImageService;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final AnswerService answerService;
    private final ImageService imageService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

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
                images.add(imageService.saveImage(file));
            }
        }
        question.updateImages(images);

        return questionRepository.save(question);
    }

    public List<QuestionListReadDto> getQuestions(Long grade, Long subject, Long detailSubject, int page) {
        List<Question> questions;

        if(subject == 0) {
            if(grade == 0) {
                questions = questionRepository.findAllByOrderByIdDes(PageRequest.of(page, 15));
            } else {
                questions = questionRepository.findAllByGradeOrderByIdDes(grade, PageRequest.of(page, 15));
            }
        } else {
            if(detailSubject == 0) {
                if(grade == 0) {
                    questions = questionRepository.findAllBySubjectOrderByIdDes(subject, PageRequest.of(page, 15));
                } else {
                    questions = questionRepository.findAllBySubjectAndGradeOrderByIdDesc(subject, grade, PageRequest.of(page, 15));
                }
            } else {
                if(grade == 0) {
                    questions = questionRepository.findAllBySubjectAndDetailSubjectOrderByIdDes(subject, detailSubject, PageRequest.of(page, 15));
                } else {
                    questions = questionRepository.findAllByGradeAndSubjectAndDetailSubjectOrderByIdDesc(grade, subject, detailSubject, PageRequest.of(page, 15));
                }
            }
        }

        return makeToQuestionReadDto(questions);
    }

    public List<QuestionListReadDto> getSearchResults(String keyword, int page) {
        List<Question> questions = questionRepository.findAllByTitleAndContentContainingOrderByIdDesc(keyword, PageRequest.of(page, 15));
        return makeToQuestionReadDto(questions);
    }

    public QuestionReadDto readById(Long id, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member writer = question.getMember();
        QuestionReadDto questionReadDto = new QuestionReadDto(
                new MemberProfileDto(writer.getGrade(), writer.getImage() != null? writer.getImage().getImagePath() : null, writer.getNickname()),
                question.getTitle(), question.getContent(), question.getImages().stream().map(Image::getImagePath).toList(), question.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                likeService.countByQuestion(question), answerService.countByQuestion(question),
                likeService.isLikedQuestion(question, member), bookmarkService.isBookmarkedQuestion(question, member)
        );
        return questionReadDto;
    }

    public List<QuestionListReadDto> makeToQuestionReadDto(List<Question> questions) {
        List<QuestionListReadDto> questionListReadDtos = new ArrayList<>();
        for(Question question : questions) {
            Member writer = question.getMember();
            QuestionListReadDto questionListReadDto = new QuestionListReadDto(
                    new MemberProfileDto(writer.getGrade(), writer.getImage() != null? writer.getImage().getImagePath() : null, writer.getNickname()),
                    question.getTitle(), answerService.checkIfSelected(question), likeService.countByQuestion(question), answerService.countByQuestion(question), question.getCreatedTime()
            );
            questionListReadDtos.add(questionListReadDto);
        }

        return questionListReadDtos;
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
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        if (question.getMember() != member) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        if (!question.getImages().isEmpty()) {
            for (Image image : question.getImages()) {
                imageService.deleteImage(image.getId());
            }
        }

        bookmarkService.deleteAllByQuestion(question);
        likeService.deleteAllByQuestion(question);
        questionRepository.delete(question);
    }
}
