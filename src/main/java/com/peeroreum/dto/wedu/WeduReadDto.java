package com.peeroreum.dto.wedu;

import lombok.Builder;
import lombok.Data;

@Data
public class WeduReadDto {
    String title;
    String imageUrl;
    Long dDay;
    String challenge;
    Long progress;
    boolean isLocked;
    String password;

    @Builder
    WeduReadDto(String title, String imageUrl, Long dDay, boolean isLocked, String password, String challenge, Long progress) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.dDay = dDay;
        this.challenge = challenge;
        this.progress = progress;
        this.isLocked = isLocked;
        this.password = password;
    }

}
