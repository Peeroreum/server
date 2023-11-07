package com.peeroreum.dto.wedu;

import lombok.Data;

@Data
public class InvitationDto {
    private String content;
    private String backgroundColor;
    private String textColor;

    public InvitationDto(String content, String backgroundColor, String textColor) {
        this.content = content;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }
}
