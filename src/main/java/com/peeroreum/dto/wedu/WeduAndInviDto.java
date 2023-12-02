package com.peeroreum.dto.wedu;

import com.peeroreum.domain.Invitation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class WeduAndInviDto {
    private String challenge;
    private List<String> hashTags;
    private String invitationUrl;

    @Builder
    WeduAndInviDto(String challenge, List<String> hashTags, Invitation invitation) {
        this.challenge = challenge;
        this.hashTags = hashTags;
        this.invitationUrl = invitation.getImage().getImagePath();
    }
}
