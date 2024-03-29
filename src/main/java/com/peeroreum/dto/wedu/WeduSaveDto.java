package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class WeduSaveDto {

    private String title;
    private Long subject;
    private String targetDate;
    private Long grade;
    private int maximumPeople;
    private String challenge;
    private int isLocked;
    private String password;
    private MultipartFile file;
    private List<String> hashTags;
    private MultipartFile inviFile;
}
