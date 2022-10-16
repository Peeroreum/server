package com.example.demo.dto.question;

import com.example.demo.domain.Question;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Data
public class QuestionReadDto {
    private Long id;
    private String content;
    private String subject;
    private String memberNickname;
    private String memberTier;
    private Long likes;
    private List<String> imageUris;
    private LocalDateTime createdTime;


    public QuestionReadDto(Question question, List<String> imageUris) {
        this.id = question.getId();
        this.content = question.getContent();
        this.subject = question.getSubject();
        this.memberNickname = question.getMember().getNickname();
        this.memberTier = question.getMember().getTier();
        this.likes = question.getLikes();
        this.imageUris = imageUris;
        this.createdTime = question.getCreatedTime();
    }

}
