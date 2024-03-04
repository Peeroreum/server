package com.peeroreum.dto.question;

import com.peeroreum.dto.member.MemberProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class QuestionListReadDto {
    private Long id;
    private MemberProfileDto memberProfileDto;
    private String title;
    private String content;
    private boolean isSelected;
    private Long likes;
    private Long comments;
    private LocalDateTime createdTime;
}
