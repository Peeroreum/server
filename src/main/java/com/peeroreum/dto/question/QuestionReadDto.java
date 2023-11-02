package com.peeroreum.dto.question;

import com.peeroreum.domain.Question;
import lombok.Data;
import lombok.Getter;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Data
public class QuestionReadDto {
    private boolean writtenByUser;
    private boolean liked;
    private Long id;
    private String content;
    private Long subject;
    private String memberNickname;
    private Long memberGrade;
    private Long likes;
    private Long dislikes;
    private Long answerCount;
    private List<String> imagePaths;
    private String createdTime;


    public QuestionReadDto(String username, boolean liked, Question question, List<String> imagePaths, Long answerCount) {
        this.writtenByUser = question.getMember().getUsername().equals(username);
        this.liked = liked;
        this.id = question.getId();
        this.content = question.getContent();
        this.subject = question.getSubject();
        this.memberNickname = question.getMember().getNickname();
        this.memberGrade = question.getMember().getGrade();
        this.likes = question.getLikes();
        this.answerCount = answerCount;
        this.imagePaths = imagePaths;
        if(question.getCreatedTime().getYear() != Year.now().getValue())
            this.createdTime = question.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        else this.createdTime = question.getCreatedTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }

}
