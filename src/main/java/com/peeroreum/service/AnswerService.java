package com.peeroreum.service;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Question;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.answer.AnswerSaveDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.service.attachment.ImageService;
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
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;


    public Answer create(AnswerSaveDto answerSaveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Question question = questionRepository.findById(answerSaveDto.getQuestionId()).orElseThrow(() -> new CustomException(ExceptionType.QUESTION_NOT_FOUND_EXCEPTION));

        Answer answer = new Answer(
                answerSaveDto.getContent(),
                member, question,
                answerSaveDto.getParentAnswerId()
        );

        List<Image> images = new ArrayList<>();
        if(!answerSaveDto.getFiles().isEmpty()) {
            for(MultipartFile file : answerSaveDto.getFiles()) {
                images.add(imageService.saveImage(file));
            }
        }
        answer.updateImages(images);

        answer = answerRepository.save(answer);
        answer.updateGroupId();

        return answerRepository.save(answer);
    }
}
