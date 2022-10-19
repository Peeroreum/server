package com.example.demo.dto.ranking;

import com.example.demo.domain.Member;
import lombok.Data;

@Data
public class RankingDto {
    String memberNickname;
    Long memberGrade;
    Long questionCnt;
    Long answerCnt;
    Long rank;

    public RankingDto(Member member, Long questionCnt, Long answerCnt, Long rank) {
        this.memberNickname = member.getNickname();
        this.memberGrade = member.getGrade();
        this.questionCnt = questionCnt;
        this.answerCnt = answerCnt;
        this.rank = rank;
    }
}
