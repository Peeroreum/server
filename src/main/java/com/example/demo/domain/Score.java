package com.example.demo.domain;

import lombok.Data;

@Data
public class Score {
    private double score;

    public Score(Long questionCnt, Long answerCnt, Long answerLike, Long answerDislike, Long durationTime) {
        this.score = questionCnt + answerCnt + durationTime;
        if(answerLike > answerDislike)
            this.score += (answerLike - answerDislike) * 0.5;
    }
}
