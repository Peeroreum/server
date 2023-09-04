package com.peeroreum.dto.question;

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
    private Long subject;
    private Long grade;
    private List<MultipartFile> files = new ArrayList<>();
}
