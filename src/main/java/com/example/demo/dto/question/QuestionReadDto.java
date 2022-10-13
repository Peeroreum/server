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
    private List<Long> fileId;
    private LocalDateTime createdTime;


    public QuestionReadDto(Question question, List<Long> fileId) {
        this.id = question.getId();
        this.content = question.getContent();
        this.subject = question.getSubject();
        this.memberNickname = question.getMember().getNickname();
        this.fileId = fileId;
        this.createdTime = question.getCreatedTime();
    }

}
