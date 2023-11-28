package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class WeduSaveDto {

    private String title;
    private Long subject;
    private String targetDate;
    private Long grade;
    private int maximumPeople;
    private Long challenge;
    private int isLocked;
    private String password;
    private MultipartFile file;
    private List<String> hashTags;
}
