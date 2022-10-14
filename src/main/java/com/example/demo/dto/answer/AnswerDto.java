package com.example.demo.dto.answer;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Member;
import com.example.demo.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnswerDto {
    private String content;
    private Member member;
    private Question parent;
    private LocalDateTime createdTime;

    public AnswerDto(Answer answer) {
        this.content = answer.getContent();
        this.member = answer.getMember();
        this.parent = answer.getQuestion();
        this.createdTime = answer.getCreatedTime();
    }

}
