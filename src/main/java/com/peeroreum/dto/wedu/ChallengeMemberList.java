package com.peeroreum.dto.wedu;

import com.peeroreum.dto.member.MemberProfileDto;
import lombok.Data;

import java.util.List;

@Data
public class ChallengeMemberList {
    List<MemberProfileDto> successMembers;
    List<MemberProfileDto> failMembers;

    public ChallengeMemberList(List<MemberProfileDto> successMembers, List<MemberProfileDto> failMembers) {
        this.successMembers = successMembers;
        this.failMembers = failMembers;
    }
}
