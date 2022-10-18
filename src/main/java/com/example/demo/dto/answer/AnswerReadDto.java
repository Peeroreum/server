package com.example.demo.dto.answer;

import com.example.demo.domain.Answer;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
public class AnswerReadDto {
    private Long id;
    private String content;
    private String memberNickname;
    private Long memberGrade;
    private Long parentId;
    private Long likes;
    private Long dislikes;
    private Boolean isDeleted;
    private List<String> imagePaths;
    private LocalDateTime createdTime;

    public AnswerReadDto(Answer answer, List<String> imagePaths) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.memberNickname = answer.getMember().getNickname();
        this.memberGrade = answer.getMember().getGrade();
        if (answer.getParent() != null)
            this.parentId = answer.getParent().getId();
        else this.parentId = 0L;
        this.likes = answer.getLikes();
        this.dislikes = answer.getDislikes();
        this.isDeleted = answer.isDeleted();
        this.imagePaths = imagePaths;
        this.createdTime = answer.getCreatedTime();
    }
}
