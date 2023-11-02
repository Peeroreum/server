package com.peeroreum.dto.wedu;

import lombok.Builder;
import lombok.Data;

@Data
public class WeduReadDto {
    String title;
    String imageUrl;
    Long dDay;
    Long challenge;

    @Builder
    WeduReadDto(String title, String imageUrl, Long dDay, Long challenge) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.dDay = dDay;
        this.challenge = challenge;
    }

}