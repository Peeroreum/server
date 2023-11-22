package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Wedu;
import com.peeroreum.domain.image.ChallengeImage;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.wedu.ChallengeReadDto;
import com.peeroreum.repository.ChallengeImageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeImageService {
    private final ChallengeImageRepository challengeImageRepository;

    public ChallengeImageService(ChallengeImageRepository challengeImageRepository) {
        this.challengeImageRepository = challengeImageRepository;
    }

    public void createChallengeImages(Member member, Wedu wedu, List<Image> proofImages) {
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
}
