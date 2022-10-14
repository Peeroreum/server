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
    private Long parentId;
    private List<Long> fileId;
    private LocalDateTime createdTime;

    public AnswerReadDto(Answer answer, List<Long> fileId) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.memberNickname = answer.getMember().getNickname();
        if (answer.getParent() != null)
            this.parentId = answer.getParent().getId();
        else this.parentId = 0L;
        this.fileId = fileId;
        this.createdTime = answer.getCreatedTime();
    }
}
