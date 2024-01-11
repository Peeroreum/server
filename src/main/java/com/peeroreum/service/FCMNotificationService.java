package com.peeroreum.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.peeroreum.domain.Member;
import com.peeroreum.dto.notification.FCMNotificationRequestDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Member targetMember = memberRepository.findByNickname(requestDto.getNickname()).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(targetMember.getFirebaseToken() != null) {
            Notification notification = Notification.builder()
                    .setTitle(requestDto.getTitle())
                    .setBody(requestDto.getBody())
                    .build();
            Message message = Message.builder()
                    .setToken(targetMember.getFirebaseToken())
                    .setNotification(notification)
                    .build();
            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();;
                return "알림 보내기를 실패하였습니다. targetMember = " + targetMember.getNickname();
            }
        }

        return "알림을 성공적으로 전송했습니다. targetMember = " + targetMember.getNickname();
    }

}
