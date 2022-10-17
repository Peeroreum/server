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
    private Long subject;
    private String memberNickname;
    private Long memberGrade;
    private Long likes;
    private Long dislikes;
    private Long answerCount;
    private List<String> imageUris;
    private LocalDateTime createdTime;


    public QuestionReadDto(Question question, List<String> imageUris, Long answerCount) {
        this.id = question.getId();
        this.content = question.getContent();
        this.subject = question.getSubject();
        this.memberNickname = question.getMember().getNickname();
        this.memberGrade = question.getMember().getGrade();
        this.likes = question.getLikes();
        this.dislikes = question.getDislikes();
        this.answerCount = answerCount;
        this.imageUris = imageUris;
        this.createdTime = question.getCreatedTime();
    }

}
