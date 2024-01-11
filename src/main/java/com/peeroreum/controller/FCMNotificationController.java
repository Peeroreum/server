package com.peeroreum.controller;

import com.peeroreum.dto.notification.FCMNotificationRequestDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class FCMNotificationController {
    private FCMNotificationService fcmNotificationService;

    @PostMapping("/member/notification")
    public ResponseDto sendNotificationByToken(@RequestBody FCMNotificationRequestDto fcmNotificationRequestDto, Principal principal) {
        return ResponseDto.success(fcmNotificationService.sendNotificationByToken(fcmNotificationRequestDto));
    }
}
