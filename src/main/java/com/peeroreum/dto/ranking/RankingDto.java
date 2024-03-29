package com.peeroreum.dto.ranking;

import com.peeroreum.domain.Member;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class RankingDto {
    String date;
    String memberNickname;
    Long memberGrade;
    Long questionCnt;
    Long answerCnt;
    int rank;

    public RankingDto(Member member, Long questionCnt, Long answerCnt, int rank) {
        this.date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM월 dd일"));
        this.memberNickname = member.getNickname();
        this.memberGrade = member.getGrade();
        this.questionCnt = questionCnt;
        this.answerCnt = answerCnt;
        this.rank = rank;
    }
}
