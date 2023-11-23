package com.peeroreum.dto.wedu;

import com.peeroreum.dto.member.MemberProfileDto;
import lombok.Data;

import java.util.List;

@Data
public class ChallengeMemberList {
    Long progress;
    List<MemberProfileDto> successMembers;
    List<MemberProfileDto> failMembers;

    public ChallengeMemberList(List<MemberProfileDto> successMembers, List<MemberProfileDto> failMembers) {
        this.progress = Math.round((double)successMembers.size() / (double)(successMembers.size() + failMembers.size()) * 100);
        this.successMembers = successMembers;
        this.failMembers = failMembers;
    }
}
