package com.example.demo.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AnswerSaveDto {
    private String content;
    private Long questionId;
    private Long parentId;
    private List<MultipartFile> files = new ArrayList<>();
}
