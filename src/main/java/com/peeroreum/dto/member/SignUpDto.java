package com.peeroreum.dto.member;

import com.peeroreum.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpDto {
    private String username;
    private String password;
    private String nickname;
    private Long grade;

    public static Member toEntity(SignUpDto signUpDto) {
        return Member.builder()
                .username(signUpDto.username)
                .password(signUpDto.password)
                .nickname(signUpDto.nickname)
                .grade(signUpDto.grade)
                .build();
    }
}