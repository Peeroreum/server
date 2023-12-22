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
    private Long goodSubject;
    private Long goodDetailSubject;
    private Long goodLevel;
    private Long badSubject;
    private Long badDetailSubject;
    private Long badLevel;
    private String school;

    public static Member toEntity(SignUpDto signUpDto) {
        return Member.builder()
                .username(signUpDto.username)
                .password(signUpDto.password)
                .nickname(signUpDto.nickname)
                .grade(signUpDto.grade)
                .goodSubject(signUpDto.goodSubject)
                .goodDetailSubject(signUpDto.goodDetailSubject)
                .goodLevel(signUpDto.goodLevel)
                .badSubject(signUpDto.badSubject)
                .badDetailSubject(signUpDto.badDetailSubject)
                .badLevel(signUpDto.badLevel)
                .school(signUpDto.school)
                .build();
    }
}
