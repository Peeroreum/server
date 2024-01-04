package com.peeroreum.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogInDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String nickname;
}
