package com.peeroreum.service;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.answer.AnswerReadDto;
import com.peeroreum.dto.answer.AnswerSaveDto;
import com.peeroreum.dto.member.MemberProfileDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.service.attachment.ImageService;
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
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final LikeService likeService;


    public Answer create(AnswerSaveDto answerSaveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(answerSaveDto.getQuestionId()).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        Answer answer = new Answer(
                answerSaveDto.getContent(),
                member, question,
                answerSaveDto.getParentAnswerId()
        );

        List<Image> images = new ArrayList<>();
        if(answerSaveDto.getFiles() != null) {
            for(MultipartFile file : answerSaveDto.getFiles()) {
                images.add(imageService.saveImage(file));
            }
        }
        answer.updateImages(images);

        answer = answerRepository.save(answer);
        answer.updateGroupId();

        return answerRepository.save(answer);
    }

    public List<AnswerReadDto> readAll(Long questionId, int page, String username) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        List<Answer> answers = answerRepository.findAllByQuestionOrderByGroupIdAscIdAsc(question, PageRequest.of(page, 10));
        return toAnswerReadList(answers, member);
    }

    public List<AnswerReadDto> toAnswerReadList(List<Answer> answers, Member member) {
        List<AnswerReadDto> answerReadDtos = new ArrayList<>();

        for(Answer answer : answers) {
            Member writer = answer.getMember();
            AnswerReadDto answerReadDto = new AnswerReadDto(
                    new MemberProfileDto(writer.getGrade(), writer.getImage() != null? writer.getImage().getImagePath() : null, writer.getNickname()),
                    answer.getId(), answer.getContent(), answer.getImages().stream().map(Image::getImagePath).toList(), answer.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                    answer.isSelected(), answer.isDeleted(), likeService.isLikedAnswer(answer, member), answer.getParentAnswerId(), likeService.countByAnswer(answer), (answer.getParentAnswerId() == -1)? answerRepository.countAllByParentAnswerId(answer.getId()) : 0
            );
            answerReadDtos.add(answerReadDto);
        }

        return answerReadDtos;
    }

    public Long countByQuestion(Question question) {
        return answerRepository.countAllByQuestion(question);
    }

    public boolean checkIfSelected(Question question) {
        return answerRepository.existsByQuestionAndIsSelected(question, true);
    }

    public void delete(Long id, String name) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.ANSWER_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(name).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(answer.getMember() != member) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        if(answer.isSelected()) {
            throw new CustomException(ExceptionType.CANNOT_DELETE_SELECTED_ANSWER);
        }

        for(Image image : answer.getImages()) {
            imageService.deleteImage(image.getId());
        }

        likeService.deleteAllByAnswer(answer);

        if(answer.getParentAnswerId() == -1 && answerRepository.existsByParentAnswerId(answer.getId())) {
            answer.delete();
            answerRepository.save(answer);
        } else {
            answerRepository.delete(answer);
        }
    }

    public void deleteAllByQuestion(Question question) {
        List<Answer> answers = answerRepository.findAllByQuestion(question);
        deleteAll(answers);
    }

    private void deleteAll(List<Answer> answers) {
        for(Answer answer : answers) {
            if(answer.isSelected()) {
                throw new CustomException(ExceptionType.CANNOT_DELETE_SELECTED_ANSWER);
            }
            for(Image image : answer.getImages()) {
                imageService.deleteImage(image.getId());
            }
            likeService.deleteAllByAnswer(answer);
            answerRepository.delete(answer);
        }
    }
}
