package com.example.demo.dto.question;

import com.example.demo.domain.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionSimpleDto {
    private String content;
    private Member member;
    private LocalDateTime createdTime;
}
