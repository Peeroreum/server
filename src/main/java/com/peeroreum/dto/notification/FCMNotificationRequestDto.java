package com.peeroreum.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private String nickname;
    private String title;
    private String body;

    @Builder
    public FCMNotificationRequestDto(String nickname, String title, String body) {
        this.nickname = nickname;
        this.title = title;
        this.body = body;
    }
}
