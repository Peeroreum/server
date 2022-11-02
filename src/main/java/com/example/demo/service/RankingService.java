package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Member;
import com.example.demo.dto.ranking.ScoreDto;
import com.example.demo.dto.ranking.RankingDto;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionType;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RankingService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private List<RankingDto> rankingList;

    //    @Scheduled(cron = "* * * * * *")
    public void init() {
        rankingList = new ArrayList<>();
        HashMap<ScoreDto, Double> scoreMap = new HashMap<>();
        List<Member> memberList = memberRepository.findAll();
        long questionCnt, answerCnt, answerLike = 0L, answerDislike = 0L, durationTime;
        double score;
        ScoreDto scoreDto;
        for (Member member : memberList) {
            questionCnt = questionRepository.countByMemberId(member.getId());

            List<Answer> answers = answerRepository.findAllByMemberId(member.getId());
            answerCnt = answers.size();
            if(answerCnt > 0) {
                for (Answer answer : answers) {
                    answerLike += answer.getLikes();
                    answerDislike += answer.getDislikes();
                }
            }

            durationTime = member.getDurationTime();
            scoreDto = new ScoreDto(member, questionCnt, answerCnt, answerLike, answerDislike, durationTime);
//            if(answerLike > answerDislike)
//                score = questionCnt + answerCnt + (durationTime / 30.0) + ((answerLike - answerDislike) * 0.5);
//            else
            score = questionCnt + answerCnt + (durationTime / 30.0);
            scoreMap.put(scoreDto, score);
        }
        List<Map.Entry<ScoreDto, Double>> scoreList = new LinkedList<>(scoreMap.entrySet());
        scoreList.sort((o1, o2) -> {
            double value = o2.getValue() - o1.getValue();
            if(value > 0)
                return 1;
            else if (value == 0)
                return 0;
            else return -1;
        });

        int size = scoreList.size();
        for (int i = 0; i < size; i++) {
            ScoreDto scores = scoreList.get(i).getKey();
            rankingList.add(new RankingDto(scores.getMember(), scores.getQuestionCnt(), scores.getAnswerCnt(), i + 1));
        }
    }


    public RankingDto getMyRanking(String username) {
        init();
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        for(RankingDto rankingDto : rankingList) {
            if(rankingDto.getMemberNickname().equals(member.getNickname()))
                return rankingDto;
        }
        return null;
    }

    public List<RankingDto> getRankingList() {
        init();
        return rankingList;
    }

}