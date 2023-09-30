package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class WeduUpdateDto {
    private String title;
    private MultipartFile image;
    private int maximumPeople;
    private boolean isSearchable;
    private boolean isLocked;
    private String password;
    private Long grade;
    private Long subject;
    private Long gender;

}
