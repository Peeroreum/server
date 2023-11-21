package com.peeroreum.dto.wedu;

import lombok.Data;

import java.util.List;

@Data
public class ChallengeReadDto {
    List<String> imageUrls;

    public ChallengeReadDto(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
