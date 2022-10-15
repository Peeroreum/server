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
    private String memberTier;
    private Long parentId;
    private List<String> fileUri;
    private LocalDateTime createdTime;

    public AnswerReadDto(Answer answer, List<String> fileUri) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.memberNickname = answer.getMember().getNickname();
        this.memberTier = answer.getMember().getTier();
        if (answer.getParent() != null)
            this.parentId = answer.getParent().getId();
        else this.parentId = 0L;
        this.fileUri = fileUri;
        this.createdTime = answer.getCreatedTime();
    }
}
