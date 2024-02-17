package com.peeroreum.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionUpdateDto {
    private String content;
    private List<MultipartFile> files;
}
