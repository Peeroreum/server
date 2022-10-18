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
    private Long memberGrade;
    private Long likes;
    private Long dislikes;
    private Long answerCount;
    private String thumbnailUri;
    private LocalDateTime createdTime;

    public QuestionListDto(Question question, Long answerCount) {
        this.id = question.getId();
        this.content = question.getContent();
        this.memberNickname = question.getMember().getNickname();
        this.memberGrade = question.getMember().getGrade();
        this.likes = question.getLikes();
        this.dislikes = question.getDislikes();
        this.answerCount = answerCount;

        this.createdTime = question.getCreatedTime();
        if(!question.getImages().isEmpty())
            this.thumbnailUri = question.getImages().get(0).getImagePath();
        else this.thumbnailUri = "";
    }
}
