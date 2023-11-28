package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class WeduUpdateDto {
    private MultipartFile image;
    private int maximumPeople;
    private boolean isLocked;
    private String password;
    private List<String> hashTags;

}
