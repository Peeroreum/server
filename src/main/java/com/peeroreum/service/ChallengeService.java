package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Wedu;
import com.peeroreum.domain.image.ChallengeImage;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.member.MemberProfileDto;
import com.peeroreum.dto.wedu.ChallengeMemberList;
import com.peeroreum.dto.wedu.ChallengeReadDto;
import com.peeroreum.dto.wedu.WeduMonthlyProgressDto;
import com.peeroreum.repository.ChallengeImageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeService {
    private final ChallengeImageRepository challengeImageRepository;

    public ChallengeService(ChallengeImageRepository challengeImageRepository) {
        this.challengeImageRepository = challengeImageRepository;
    }

    public void createChallengeImages(Member member, Wedu wedu, List<Image> proofImages) {
        if(challengeImageRepository.existsByWeduAndMemberAndChallengeDate(wedu, member, LocalDate.now())) {
            challengeImageRepository.deleteByWeduAndMemberAndChallengeDate(wedu, member, LocalDate.now());
        }
        ChallengeImage challengeImage = ChallengeImage.builder()
                .member(member)
                .wedu(wedu)
                .image(proofImages)
                .challengeDate(LocalDate.now())
                .build();
        challengeImageRepository.save(challengeImage);
    }

    public ChallengeReadDto readChallengeImages(Wedu wedu, Member member, LocalDate formattedDate) {
        List<String> imageUrls = challengeImageRepository.findAllByMemberAndWeduAndChallengeDate(member, wedu, formattedDate)
                .getImage()
                .stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());

        return new ChallengeReadDto(imageUrls);
    }

    public ChallengeMemberList readChallengeMembers(List<Member> allMembers, Wedu wedu, LocalDate formattedDate) {
        List<ChallengeImage> challengeImages = challengeImageRepository.findAllByWeduAndChallengeDate(wedu, formattedDate);
        List<Member> successMembers = challengeImages.stream().map(ChallengeImage::getMember).collect(Collectors.toList());
        List<MemberProfileDto> successMemberProfiles = new ArrayList<>();
        for(Member member : successMembers) {
            successMemberProfiles.add(new MemberProfileDto(member.getGrade(), member.getImage(), member.getNickname()));
        }
        List<MemberProfileDto> failMemberProfiles = new ArrayList<>();
        for(Member member : allMembers) {
            if(!successMembers.contains(member)) {
                failMemberProfiles.add(new MemberProfileDto(member.getGrade(), member.getImage(), member.getNickname()));
            }
        }

        return new ChallengeMemberList(successMemberProfiles, failMemberProfiles);
    }

    public List<Long> readMonthlyProgress(Wedu wedu, int total, LocalDate formattedDate) {
        int month = formattedDate.getMonthValue();
        int days = getDays(month);
        List<Long> progressList = new ArrayList<>();
        for(int i = 1; i <= days; i++) {
            Long success = challengeImageRepository.countAllByWeduAndChallengeDate(wedu, LocalDate.of(formattedDate.getYear(), month, i));
            progressList.add(Math.round((double)success / (double)total * 100));
        }
        return progressList;
    }

    public void deleteChallengeImages(Wedu wedu) {
        challengeImageRepository.deleteAllByWedu(wedu);
    }

    public Long getTodayProgress(Wedu wedu, int total) {
        Long success = challengeImageRepository.countAllByWeduAndChallengeDate(wedu, LocalDate.now());
        return Math.round((double)success / (double)total * 100);
    }

    private int getDays(int month) {
        if(month == 2) {
            return 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }
}
