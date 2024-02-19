package com.peeroreum.dto.answer;

import com.peeroreum.dto.member.MemberProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerReadDto {
    private MemberProfileDto memberProfileDto;
    private Long id;
    private String content;
    private List<String> images;
    private String createdTime;
    private Boolean isSelected;
    private Boolean isDeleted;
    private Boolean isLiked;
    private Long parentId;
    private Long likes;
    private Long comments;
}
