package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.MemberWedu;
import com.peeroreum.domain.Wedu;
import com.peeroreum.domain.image.ChallengeImage;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.member.MemberProfileDto;
import com.peeroreum.dto.wedu.ChallengeMemberList;
import com.peeroreum.dto.wedu.ChallengeReadDto;
import com.peeroreum.repository.ChallengeImageRepository;
import com.peeroreum.repository.MemberWeduRepository;
import com.peeroreum.service.attachment.ImageService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService {
    private final ChallengeImageRepository challengeImageRepository;
    private final MemberWeduRepository memberWeduRepository;
    private final ImageService imageService;

    public ChallengeService(ChallengeImageRepository challengeImageRepository, MemberWeduRepository memberWeduRepository, ImageService imageService) {
        this.challengeImageRepository = challengeImageRepository;
        this.memberWeduRepository = memberWeduRepository;
        this.imageService = imageService;
    }

    public void createChallengeImages(Member member, Wedu wedu, List<Image> proofImages) {
        MemberWedu memberWedu = memberWeduRepository.findByMemberAndWedu(member, wedu);

//        List<ChallengeImage> challengeImages = challengeImageRepository.findAllByMemberAndWeduOrderByChallengeDateDesc(member, wedu);
//        if(!challengeImages.isEmpty() && challengeImages.get(0).getChallengeDate().plusDays(1).equals(now)) {
//            continuity = memberWedu.getContinuousDate() + 1;
//        }
        ChallengeImage challengeImage = ChallengeImage.builder()
                .member(member)
                .wedu(wedu)
                .image(proofImages)
                .challengeDate(LocalDate.now())
                .build();

        memberWedu.addContinuousDate();
        memberWeduRepository.save(memberWedu);
        challengeImageRepository.save(challengeImage);
    }

    public int getContinuousDate(Member member, Wedu wedu) {
        LocalDate now = LocalDate.now();
        MemberWedu memberWedu = memberWeduRepository.findByMemberAndWedu(member, wedu);
        List<ChallengeImage> challengeImages = challengeImageRepository.findAllByMemberAndWeduOrderByChallengeDateDesc(member, wedu);
        if(challengeImages.isEmpty() || (!challengeImages.get(0).getChallengeDate().equals(now) && !challengeImages.get(0).getChallengeDate().plusDays(1).equals(now))
        ) {
            memberWedu.clearContinuousDate();
            memberWeduRepository.save(memberWedu);
        }
        return memberWedu.getContinuousDate();
    }

    public ChallengeReadDto readChallengeImages(Wedu wedu, Member member, LocalDate formattedDate) {
        List<String> imageUrls = Optional.ofNullable(challengeImageRepository.findAllByMemberAndWeduAndChallengeDate(member, wedu, formattedDate))
                .map(ChallengeImage::getImage)
                .map(images -> images.stream().map(Image::getImagePath).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return new ChallengeReadDto(imageUrls);
    }

    public void deleteTodayChallengeImages(Member member, Wedu wedu) {
        LocalDate now = LocalDate.now();
        ChallengeImage challengeImage = challengeImageRepository.findAllByMemberAndWeduAndChallengeDate(member, wedu, now);
        List<Image> images = challengeImage.getImage();
        for(Image image : images) {
            imageService.deleteImage(image.getId());
        }

        MemberWedu memberWedu = memberWeduRepository.findByMemberAndWedu(member, wedu);
        memberWedu.subContinuousDate();
        memberWeduRepository.save(memberWedu);

        challengeImageRepository.deleteAllByMemberAndWeduAndChallengeDate(member, wedu, now);
    }

    public ChallengeMemberList readChallengeMembers(Wedu wedu, LocalDate formattedDate) {
        List<MemberWedu> memberWedus = memberWeduRepository.findAllByWeduAndCreatedTimeBefore(wedu, formattedDate.atTime(LocalTime.of(23, 59)));
        List<Member> allMembers = memberWedus.stream().map(MemberWedu::getMember).toList();
        List<ChallengeImage> challengeImages = challengeImageRepository.findAllByWeduAndChallengeDate(wedu, formattedDate);
        List<Member> successMembers = challengeImages.stream().map(ChallengeImage::getMember).toList();
        List<MemberProfileDto> successMemberProfiles = new ArrayList<>();
        for(Member member : successMembers) {
            successMemberProfiles.add(new MemberProfileDto(member.getGrade(), member.getImage() == null? null : member.getImage().getImagePath(), member.getNickname()));
        }
        List<MemberProfileDto> failMemberProfiles = new ArrayList<>();
        for(Member member : allMembers) {
            if(!successMembers.contains(member)) {
                failMemberProfiles.add(new MemberProfileDto(member.getGrade(), member.getImage() == null? null : member.getImage().getImagePath(), member.getNickname()));
            }
        }

        return new ChallengeMemberList(successMemberProfiles, failMemberProfiles);
    }

    public List<Long> readMonthlyProgress(Wedu wedu, LocalDate formattedDate) {
        int month = formattedDate.getMonthValue();
        int days = getDays(month);
        List<Long> progressList = new ArrayList<>();
        for(int i = 1; i <= days; i++) {
            LocalDate searchDate = LocalDate.of(formattedDate.getYear(), month, i);
            int total = memberWeduRepository.countAllByWeduAndCreatedTimeBefore(wedu, searchDate.atTime(LocalTime.of(23, 59)));
            Long success = challengeImageRepository.countAllByWeduAndChallengeDate(wedu, searchDate);
            progressList.add(Math.round((double)success / (double)total * 100));
        }
        return progressList;
    }

    public void deleteTodayChallengeImages(Wedu wedu) {
        List<ChallengeImage> challengeImages = challengeImageRepository.findAllByWedu(wedu);
        if(!challengeImages.isEmpty()) {
            deleteChallengeImages(challengeImages);
            challengeImageRepository.deleteAllByWedu(wedu);
        }
    }

    public void deleteByWeduAndMember(Wedu wedu, Member member) {
        List<ChallengeImage> challengeImages = challengeImageRepository.findAllByWeduAndMember(wedu, member);
        if(!challengeImages.isEmpty()) {
            deleteChallengeImages(challengeImages);
            challengeImageRepository.deleteAllByWeduAndMember(wedu, member);
        }
    }

    public void deleteChallengeImages(List<ChallengeImage> challengeImages) {
        for(ChallengeImage challengeImage : challengeImages) {
            List<Image> images = challengeImage.getImage();
            for(Image image : images) {
                imageService.deleteImage(image.getId());
            }
        }
    }

    public Long getTodayProgress(Wedu wedu, int total) {
        Long success = challengeImageRepository.countAllByWeduAndChallengeDate(wedu, LocalDate.now());
        return Math.round((double)success / (double)total * 100);
    }

    public Long getTheDayProgress(Wedu wedu, int total) {
        Long success = challengeImageRepository.countAllByWeduAndChallengeDate(wedu, wedu.getTargetDate());
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
