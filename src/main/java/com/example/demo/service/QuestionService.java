package com.example.demo.service;

import com.example.demo.domain.Image;
import com.example.demo.domain.Question;
import com.example.demo.domain.Member;
import com.example.demo.dto.question.QuestionSaveDto;
import com.example.demo.dto.question.QuestionDto;
import com.example.demo.dto.question.QuestionSearchRequest;
import com.example.demo.dto.question.QuestionUpdateDto;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.exception.QuestionNoFoundException;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.attachment.FileHandler;
import com.example.demo.service.attachment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final FileHandler fileHandler;

    @Transactional
    public void create(QuestionSaveDto saveDto, String username) throws IOException {
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        Question question = Question.builder()
                .content(saveDto.getContent())
                .member(member)
                .subject(saveDto.getSubject())
                .build();
        List<Image> imageList = fileHandler.parseFileInfo(saveDto.getFiles());
        if(!imageList.isEmpty()) {
            for(Image image : imageList)
                question.addImage(imageRepository.save(image));
        }
        questionRepository.save(question);
    }

    public QuestionDto read(Long id) {
        return QuestionDto.toDto(questionRepository.findById(id).orElseThrow(QuestionNoFoundException::new));
    }

    public void update(Long id, QuestionUpdateDto updateDto) throws IOException {
        Question question = questionRepository.findById(id).orElseThrow(QuestionNoFoundException::new);
        question.update(updateDto.getContent(), updateDto.getSubject());
        List<Image> existImages = imageService.findAllByQuestion(id); // 이미 저장된 이미지
        List<MultipartFile> updateFiles = updateDto.getFiles(); // 업데이트 요청
        List<MultipartFile> newFiles = new ArrayList<>(); // 새로 저장할 파일

        if(CollectionUtils.isEmpty(existImages)) {
            if (!CollectionUtils.isEmpty(updateFiles)) {
                for (MultipartFile multipartFile : updateFiles)
                    newFiles.add(multipartFile);
            }
        }
        else {
            if(CollectionUtils.isEmpty(updateFiles)) {
                for(Image image : existImages)
                    imageService.deleteImage(image.getId());
            }
            else {
                List<String> existImagesName = new ArrayList<>();

                for(Image existImage : existImages) {
                    Image image = imageService.findByImageId(existImage.getId());
                    String imageName = image.getImageName();

                    if(!updateFiles.contains(imageName))
                        imageService.deleteImage(existImage.getId());
                    else
                        existImagesName.add(imageName);
                }

                for(MultipartFile multipartFile : updateFiles) {
                    String multipartOriginalName = multipartFile.getOriginalFilename();
                    if(!existImagesName.contains(multipartOriginalName))
                        newFiles.add(multipartFile);
                }
            }
        }
        List<Image> imageList = fileHandler.parseFileInfo(newFiles);
        if(!imageList.isEmpty()) {
            for(Image image : imageList)
                imageRepository.save(image);
        }
    }
    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(QuestionNoFoundException::new);
        questionRepository.delete(question);
    }

    public List<Question> list(QuestionSearchRequest searchRequest) {
        return questionRepository.findAllBySubjectAndMemberGrade(searchRequest.getSubject(), searchRequest.getGrade());
    }

}
