package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Member;
import com.example.demo.domain.Role;
import com.example.demo.dto.ranking.ScoreDto;
import com.example.demo.dto.ranking.RankingDto;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionType;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final HeartRepository heartRepository;
    private final XHeartRepository xHeartRepository;
    private final List<RankingDto> rankingList;

    @Scheduled(cron = "* * * * * *")
    public void init() {
        HashMap<ScoreDto, Double> hashMap = new HashMap<>();
        List<Member> memberList = memberRepository.findAll();
        long questionCnt, answerCnt, answerLike = 0L, answerDislike = 0L, durationTime;
        ScoreDto scoreDto;
        for(Member member : memberList) {
            questionCnt = questionRepository.countByMemberId(member.getId());
            List<Answer> answers = answerRepository.findAllByMemberId(member.getId());
            answerCnt = answers.size();
            for(Answer answer : answers) {
                answerLike += heartRepository.countByAnswer(answer);
                answerDislike += xHeartRepository.countByAnswer(answer);
            }
            durationTime = member.getDurationTime();
            scoreDto = new ScoreDto(member, questionCnt, answerCnt, answerLike, answerDislike, durationTime);

            if(member.getRoles().get(0) != Role.ADMIN) {
                hashMap.put(scoreDto, scoreDto.getScore());
            }
        }
        List<ScoreDto> scoreList = new ArrayList<>(hashMap.keySet());
        scoreList.sort(Comparator.comparing(hashMap::get).reversed());

        if(scoreList.size() < 10) {
            for(int i = 1; i <= scoreList.size(); i++) {
                ScoreDto score = scoreList.get(i);
                rankingList.add(new RankingDto(score.getMember(), score.getQuestionCnt(), score.getAnswerCnt(), i));
            }
        }
        else {
            for(int i = 1; i <= 10; i++) {
                ScoreDto score = scoreList.get(i);
                rankingList.add(new RankingDto(score.getMember(), score.getQuestionCnt(), score.getAnswerCnt(), i));
            }
        }
    }


    public RankingDto getMyRanking(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        for(RankingDto rankingDto : rankingList) {
            if(rankingDto.getMemberNickname().equals(member.getNickname()))
                return rankingDto;
        }
        return null;
    }

    public List<RankingDto> getRankingList() {
        return rankingList;
    }

}
