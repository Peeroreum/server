package com.peeroreum.dto.member;

import lombok.Data;

@Data
public class MemberProfileDto {
    private Long grade;
    private String profileImage;
    private String backgroundImage;
    private String nickname;
    private int friendNumber;
    private boolean following;
    private Long activeDaysCount;

    public MemberProfileDto(Long grade, String profileImage, String nickname) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }

    public MemberProfileDto(Long grade, String profileImage, String backgroundImage, String nickname, Long activeDaysCount) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        this.nickname = nickname;
        this.activeDaysCount = activeDaysCount;
    }

    public MemberProfileDto(Long grade, String profileImage, String backgroundImage, String nickname, int friendNumber, boolean following, Long activeDaysCount) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        this.nickname = nickname;
        this.friendNumber = friendNumber;
        this.following = following;
        this.activeDaysCount = activeDaysCount;
    }
}
