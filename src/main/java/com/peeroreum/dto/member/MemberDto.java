package com.peeroreum.dto.member;

import com.peeroreum.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String username;
    private String nickname;
    private Long grade;
    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getUsername(), member.getNickname(), member.getGrade());
    }
}
