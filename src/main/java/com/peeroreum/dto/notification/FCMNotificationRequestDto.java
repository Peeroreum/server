package com.peeroreum.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private String title;
    private String body;

    @Builder
    public FCMNotificationRequestDto(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
