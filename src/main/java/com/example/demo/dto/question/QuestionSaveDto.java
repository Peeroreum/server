package com.example.demo.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionSaveDto {
    private String content;
    private String subject;
    private List<MultipartFile> files;

}
