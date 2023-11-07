package com.peeroreum.dto.answer;

import com.peeroreum.domain.Answer;
import lombok.Data;
import lombok.Getter;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Getter
public class AnswerReadDto {
    private boolean writtenByUser;
    private boolean liked;
    private Long id;
    private String content;
    private String memberNickname;
    private Long memberGrade;
    private Long likes;
    private Long dislikes;
    private Boolean isDeleted;
    private List<String> imagePaths;
    private String createdTime;

    public AnswerReadDto(String username, boolean liked, Answer answer, List<String> imagePaths) {
        this.writtenByUser = answer.getMember().getUsername().equals(username);
        this.liked = liked;
        this.id = answer.getId();
        this.content = answer.getContent();
        this.memberNickname = answer.getMember().getNickname();
        this.memberGrade = answer.getMember().getGrade();
        this.likes = answer.getLikes();
        this.isDeleted = answer.isDeleted();
        this.imagePaths = imagePaths;
        if(answer.getCreatedTime().getYear() != Year.now().getValue())
            this.createdTime = answer.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        else this.createdTime = answer.getCreatedTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }
}
