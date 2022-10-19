package com.example.demo.service;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Member;
import com.example.demo.domain.Role;
import com.example.demo.domain.Score;
import com.example.demo.dto.ranking.RankingDto;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionType;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final HeartRepository heartRepository;
    private final XHeartRepository xHeartRepository;

    @PostConstruct
    public void init() {
        List<Member> memberList = memberRepository.findAll();
        Long questionCnt, answerCnt, answerLike = 0L, answerDislike = 0L, durationTime;
        double score;
        for(Member member : memberList) {
            questionCnt = questionRepository.countByMember(member.getId());
            List<Answer> answers = answerRepository.findAllByMember(member.getId());
            answerCnt = Long.valueOf(answers.size());
            for(Answer answer : answers) {
                answerLike += heartRepository.countByAnswer(answer.getId());
                answerDislike += xHeartRepository.countByAnswer(answer.getId());
            }
            durationTime = member.getDurationTime();
            score = new Score(questionCnt, answerCnt, answerLike, answerDislike, durationTime).getScore();
            if(member.getRoles().get(0) != Role.ADMIN) {
                redisTemplate.opsForZSet().add("ranking", member.getNickname(), score);
            }
        }
    }

    public RankingDto getMyRanking(String username) {
        Long rank = 0L;
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Double score = redisTemplate.opsForZSet().score("ranking", member.getNickname());
        Set<String> ranking2 = redisTemplate.opsForZSet().reverseRangeByScore("ranking", score, score, 0, 1);
        for(String s : ranking2) {
            rank = redisTemplate.opsForZSet().reverseRank("ranking", s);
        }
        return new RankingDto(member, questionRepository.countByMember(member.getId()), answerRepository.countByMember(member.getId()), rank+1);
    }

    public List<RankingDto> getRankingList() {
        List<RankingDto> rankingList = new ArrayList<>();
        String key = "ranking";
        Long rank = 1L;
        Member member;
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringStringZSetOperations.reverseRangeWithScores(key, 0, 10);
        for(ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            member = memberRepository.findByUsername(tuple.getValue()).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
            rankingList.add(new RankingDto(member, questionRepository.countByMember(member.getId()), answerRepository.countByMember(member.getId()), rank++));
        }
        return rankingList;
    }

    @PreDestroy
    public void destroy() {
        redisTemplate.delete("ranking");
    }
}
