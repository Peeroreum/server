package com.example.demo.dto.question;

import com.example.demo.domain.Question;
import com.example.demo.dto.Attachment.ImageDto;
import com.example.demo.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class QuestionDto {
    private String content;
    private String subject;
    private MemberDto member;
    private List<ImageDto> images;
    private LocalDateTime createdTime;

    public static QuestionDto toDto(Question question) {
        return new QuestionDto(
                question.getContent(),
                question.getSubject(),
                MemberDto.toDto(question.getMember()),
                question.getImages().stream().map(i -> ImageDto.toDto(i)).collect(Collectors.toList()),
                question.getCreatedTime()
        );
    }
}