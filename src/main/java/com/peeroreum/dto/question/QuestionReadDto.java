package com.peeroreum.dto.question;

import com.peeroreum.dto.member.MemberProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionReadDto {
    private MemberProfileDto memberProfileDto;
    private String title;
    private String content;
    private List<String> imageUrls;
    private String createdTime;
    private Long likes;
    private Long comments;
    private boolean isLiked;
    private boolean isBookmarked;
}
