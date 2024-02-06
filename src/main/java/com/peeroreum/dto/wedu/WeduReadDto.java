package com.peeroreum.dto.wedu;

import com.peeroreum.domain.HashTag;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class WeduReadDto { // 인원, 학년, 해시태크
    String title;
    String imageUrl;
    Long dDay;
    String challenge;
    Long progress;
    boolean isLocked;
    String password;
    int continuousDate;
    String hostMail;
    Long subject;
    Long grade;
    int attendingPeopleNum;
    List<String> hashTags;

    @Builder
    WeduReadDto(String title, Long subject, Long grade, String imageUrl, Long dDay, boolean isLocked, String password, String challenge, Long progress, int continuousDate, String hostMail, int attendingPeopleNum, List<String> hashTags) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.dDay = dDay;
        this.challenge = challenge;
        this.progress = progress;
        this.isLocked = isLocked;
        this.password = password;
        this.continuousDate = continuousDate;
        this.hostMail = hostMail;
        this.subject = subject;
        this.grade = grade;
        this.attendingPeopleNum = attendingPeopleNum;
        this.hashTags = hashTags;
    }

}
