package com.example.demo.dto.ranking;

import com.example.demo.domain.Member;
import lombok.Data;

@Data
public class ScoreDto {
    private Member member;
    private Long questionCnt;
    private Long answerCnt;
    private Long answerLike;
    private Long answerDislike;
    private Long durationTime;

    public ScoreDto(Member member, Long questionCnt, Long answerCnt, Long answerLike, Long answerDislike, Long durationTime) {
        this.member = member;
        this.questionCnt = questionCnt;
        this.answerCnt = answerCnt;
        this.answerLike = answerLike;
        this.answerDislike = answerDislike;
        this.durationTime = durationTime;
    }
}
