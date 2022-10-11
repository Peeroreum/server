package com.example.demo.service;

import com.example.demo.domain.Image;
import com.example.demo.domain.Question;
import com.example.demo.domain.Member;
import com.example.demo.dto.question.QuestionSaveDto;
import com.example.demo.dto.question.QuestionDto;
import com.example.demo.dto.question.QuestionUpdateDto;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.exception.QuestionNoFoundException;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Transactional
    public void create(QuestionSaveDto saveDto) {
        Member member = memberRepository.findById(saveDto.getMemberId()).orElseThrow(MemberNotFoundException::new);
        List<Image> imageList = saveDto.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(Collectors.toList());
        Question question = questionRepository.save(new Question(saveDto.getContent(), saveDto.getSubject(), member, imageList));
        uploadImages(question.getImages(), saveDto.getImages());
    }

    public QuestionDto read(Long id) {
        return QuestionDto.toDto(questionRepository.findById(id).orElseThrow(QuestionNoFoundException::new));
    }

    public void update(Long id, QuestionUpdateDto updateDto) {
        Question question = questionRepository.findById(id).orElseThrow(QuestionNoFoundException::new);
        Question.ImageUpdatedResult result = question.update(updateDto);
        uploadImages(result.getAddedImages(), result.getAddedFiles());
        deleteImages(result.getDeletedImages());
    }
    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(QuestionNoFoundException::new);
        deleteImages(question.getImages());
        questionRepository.delete(question);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> files) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(files.get(i), images.get(i).getImgName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getImgName()));
    }

}
