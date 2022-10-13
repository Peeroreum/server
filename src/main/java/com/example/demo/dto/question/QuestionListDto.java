package com.example.demo.dto.question;

import com.example.demo.domain.Question;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Data
public class QuestionListDto {
    private Long id;
    private String content;
    private String memberNickname;
    private Long thumbnailId;
    private LocalDateTime createdTime;

    public QuestionListDto(Question question) {
        this.id = question.getId();
        this.content = question.getContent();
        this.memberNickname = question.getMember().getNickname();
        this.createdTime = question.getCreatedTime();

        if(!question.getImages().isEmpty())
            this.thumbnailId = question.getImages().get(0).getId();
        else this.thumbnailId = 0L;
    }
}
