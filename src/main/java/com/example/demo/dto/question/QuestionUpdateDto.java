package com.example.demo.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateDto {
    private String content;
    private String subject;
    private List<MultipartFile> addedImages = new ArrayList<>();
    private List<Long> deletedImages = new ArrayList<>();
}
