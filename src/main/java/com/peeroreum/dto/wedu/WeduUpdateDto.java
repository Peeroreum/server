package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class WeduUpdateDto {
    private MultipartFile image;
    private int maximumPeople;
    private Long gender;
    private boolean isLocked;
    private String password;
}
