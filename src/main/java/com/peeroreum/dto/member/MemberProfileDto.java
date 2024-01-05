package com.peeroreum.dto.member;

import lombok.Data;

@Data
public class MemberProfileDto {
    private Long grade;
    private String profileImage;
    private String nickname;
    private int friendNumber;
    private boolean following;

    public MemberProfileDto(Long grade, String profileImage, String nickname) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }

    public MemberProfileDto(Long grade, String profileImage, String nickname, int friendNumber, boolean following) {
        this.grade = grade;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.friendNumber = friendNumber;
        this.following = following;
    }
}
