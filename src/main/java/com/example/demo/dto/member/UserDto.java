package com.example.demo.dto.member;

import com.example.demo.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String nickname;
    private String grade;
    private String image;
    public static UserDto toDto(Member member) {
        return new UserDto(member.getUsername(), member.getNickname(), member.getGrade(), member.getImage());
    }
}
