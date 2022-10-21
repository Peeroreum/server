package com.example.demo.dto.question;

import com.example.demo.domain.Question;
import lombok.Data;
import lombok.Getter;

import java.time.Year;
import java.time.format.DateTimeFormatter;

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
    private int imageCount;
    private String thumbnailUri;
    private String createdTime;

    public QuestionListDto(Question question, Long answerCount) {
        this.id = question.getId();
        this.content = question.getContent();
        this.memberNickname = question.getMember().getNickname();
        this.memberGrade = question.getMember().getGrade();
        this.likes = question.getLikes();
        this.dislikes = question.getDislikes();
        this.answerCount = answerCount;
        this.imageCount = question.getImages().size();

        if(question.getCreatedTime().getYear() != Year.now().getValue())
            this.createdTime = question.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        else this.createdTime = question.getCreatedTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));

        if(!question.getImages().isEmpty())
            this.thumbnailUri = question.getImages().get(0).getImagePath();
        else this.thumbnailUri = "";
    }
}
