package com.example.demo.dto.member;

import com.example.demo.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpDto {
    private String username;
    private String password;
    private String nickname;
    private String image;
    private String grade;

    private String tier;

    public static Member toEntity(SignUpDto signUpDto) {
        return Member.builder()
                .username(signUpDto.username)
                .password(signUpDto.password)
                .nickname(signUpDto.nickname)
                .image(signUpDto.image)
                .grade(signUpDto.grade)
                .tier("bronze")
                .build();
    }
}
