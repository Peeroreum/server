package com.example.demo.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QuestionSaveDto {
    private String content;

    private String subject;

    private Long memberId;
    private List<MultipartFile> images = new ArrayList<>();

    public QuestionSaveDto(QuestionSaveDto questionSaveDto) {
        this.content = questionSaveDto.content;
        this.subject = questionSaveDto.subject;
        this.memberId = questionSaveDto.memberId;
        this.images = questionSaveDto.images;
    }
}
