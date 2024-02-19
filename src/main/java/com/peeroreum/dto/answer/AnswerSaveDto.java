package com.peeroreum.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerSaveDto {
    private String content;
    private Long questionId;
    private Long parentAnswerId;
    private List<MultipartFile> files;
}
