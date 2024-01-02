package com.peeroreum.dto.member;

import lombok.Data;

@Data
public class MemberProfileDto {
    private Long grade;
    private String profileImage;
    private String nickname;
    private int friendNumber;

    public MemberProfileDto(Long grade, String profileImage, String nickname) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }

    public MemberProfileDto(Long grade, String profileImage, String nickname, int friendNumber) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.friendNumber = friendNumber;
    }
}
