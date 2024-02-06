package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class WeduUpdateDto {
    private MultipartFile image;
    private Integer maximumPeople;
    private Boolean isLocked;
    private String password;
    private List<String> hashTags;

}
