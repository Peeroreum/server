package com.peeroreum.service;

import com.peeroreum.domain.*;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.wedu.*;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import com.peeroreum.service.attachment.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class WeduService {

    private final WeduRepository weduRepository;
    private final MemberRepository memberRepository;
    private final MemberWeduRepository memberWeduRepository;
    private final InvitationRepository invitationRepository;
    private final ImageRepository imageRepository;
    private final HashTagService hashTagService;
    private final ChallengeService challengeService;
    private final InvitationService invitationService;
    private final S3Service s3Service;

    public WeduService (WeduRepository weduRepository, MemberRepository memberRepository, MemberWeduRepository memberWeduRepository, InvitationRepository invitationRepository, ImageRepository imageRepository, HashTagService hashTagService, ChallengeService challengeService, InvitationService invitationService, S3Service s3Service) {
        this.weduRepository = weduRepository;
        this.memberRepository = memberRepository;
        this.memberWeduRepository = memberWeduRepository;
        this.invitationRepository = invitationRepository;
        this.imageRepository = imageRepository;
        this.hashTagService = hashTagService;
        this.challengeService = challengeService;
        this.invitationService = invitationService;
        this.s3Service = s3Service;
    }

    public List<WeduDto> getAll(Long grade, Long subject) {
        List<Wedu> weduList = getWedus(grade, subject);
        weduList.sort((Comparator.comparing(EntityTime::getCreatedTime)).reversed());
        return getWeduDtos(weduList);
    }

    public List<WeduDto> getSearchResults(String keyword) {
        Set<Wedu> searchedWedu = new HashSet<>();
        searchedWedu.addAll(weduRepository.findAllByTitleContaining(keyword));
        searchedWedu.addAll(hashTagService.searchHashTags(keyword));
        List<WeduDto> searchResults = getWeduDtos(searchedWedu.stream().toList());
        searchResults.sort((Comparator.comparing(WeduDto::getId)).reversed());
        return searchResults;
    }

    public List<WeduDto> getAllRecommends(String username) {
        Member member = findMember(username);
        List<Wedu> weduList = getWedus(member.getGrade(), member.getGoodSubject());
        weduList.addAll(getWedus(member.getGrade(), member.getBadSubject()));
        weduList.sort((Comparator.comparing(EntityTime::getCreatedTime)).reversed());
        return getWeduDtos(weduList);
    }

    public List<WeduDto> getAllPopular(Long grade, Long subject) {
        List<Wedu> weduList = getWedus(grade, subject);
        List<WeduDto> weduDtoList = getWeduDtos(weduList);
        weduDtoList.sort(Comparator.comparing(WeduDto::getAttendingPeopleNum).reversed());
        return weduDtoList;
    }

    private List<Wedu> getWedus(Long grade, Long subject) {
        List<Wedu> weduList;
        if(grade == 0 && subject == 0) {
            weduList = weduRepository.findAll();
        } else if(grade == 0) {
            weduList = weduRepository.findAllBySubject(subject);
        } else if(subject == 0) {
            weduList = weduRepository.findAllByGrade(grade);
        } else {
            weduList = weduRepository.findAllByGradeAndSubject(grade, subject);
        }
        return weduList;
    }

    private List<WeduDto> getWeduDtos(List<Wedu> weduList) {
        List<WeduDto> weduDtoList = new ArrayList<>();
        for(Wedu wedu : weduList) {
            int attendingPeople = memberWeduRepository.countAllByWedu(wedu);
            weduDtoList.add(new WeduDto(wedu, attendingPeople));
        }
        return weduDtoList;
    }

    public MyInWeduDto getAllMy(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        List<Wedu> myWeduList = memberWeduRepository.findAllByMember(member).stream().map(MemberWedu::getWedu).toList();
        List<WeduDto> myIngWedus = new ArrayList<>();
        List<WeduDto> myEndWedus = new ArrayList<>();
        for(Wedu myWedu : myWeduList) {
            int total = memberWeduRepository.countAllByWedu(myWedu);
            Long progress = 0L;
            if(myWedu.getTargetDate().isBefore(LocalDate.now())) {
                progress = challengeService.getTheDayProgress(myWedu, total);
                myEndWedus.add(new WeduDto(myWedu, total, progress));
            } else {
                progress = challengeService.getTodayProgress(myWedu, total);
                myIngWedus.add(new WeduDto(myWedu, total, progress));
            }

        }
        return new MyInWeduDto(myIngWedus, myEndWedus);
    }

    public WeduReadDto getById(Long id, String username) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        MemberWedu memberWedu = memberWeduRepository.findByMemberAndWedu(member, wedu);
        int total = memberWeduRepository.countAllByWedu(wedu);
        Long progress = challengeService.getTodayProgress(wedu, total);

        return WeduReadDto.builder()
                .title(wedu.getTitle())
                .imageUrl(wedu.getImage() != null ? wedu.getImage().getImagePath() : null)
                .dDay(LocalDate.now().until(wedu.getTargetDate(), ChronoUnit.DAYS))
                .challenge(wedu.getChallenge())
                .continuousDate(memberWedu.getContinuousDate())
                .isLocked(wedu.isLocked())
                .progress(progress)
                .hostMail(wedu.getHost().getUsername())
                .build();
    }

    public Wedu make(WeduSaveDto weduSaveDto, String username) {
        Member host = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Image image = null;
        if(weduSaveDto.getFile() != null) {
            image = imageRepository.save(s3Service.uploadImage(weduSaveDto.getFile()));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Wedu savingWedu = Wedu.builder()
                .title(weduSaveDto.getTitle())
                .image(image)
                .host(host)
                .maximumPeople(weduSaveDto.getMaximumPeople())
                .isLocked(weduSaveDto.getIsLocked() == 1)
                .password(weduSaveDto.getPassword())
                .grade(weduSaveDto.getGrade())
                .subject(weduSaveDto.getSubject())
                .challenge(weduSaveDto.getChallenge())
                .targetDate(LocalDate.parse(weduSaveDto.getTargetDate(), formatter))
                .build();
        if(weduSaveDto.getHashTags() != null) {
            Set<HashTag> hashTagSet = hashTagService.createHashTags(savingWedu, weduSaveDto.getHashTags());
            savingWedu.setHashTags(hashTagSet);
        }
        Wedu wedu = weduRepository.save(savingWedu);
        memberWeduRepository.save(MemberWedu.builder().member(host).wedu(wedu).build());
        makeInvitation(wedu.getId(), weduSaveDto.getInviFile());
        return wedu;
    }

    public Wedu update(Long id, WeduUpdateDto weduUpdateDto, String username) {
        Member member = findMember(username);
        Wedu existingWedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));

        if(member != existingWedu.getHost()) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        Image image = existingWedu.getImage();
        if(!weduUpdateDto.getImage().isEmpty()) {
            s3Service.deleteImage(image.getImageName());
            imageRepository.delete(image);
            image = imageRepository.save(s3Service.uploadImage(weduUpdateDto.getImage()));
        }
        existingWedu.update(image, weduUpdateDto.getMaximumPeople(), weduUpdateDto.isLocked(), weduUpdateDto.getPassword());
        hashTagService.deleteHashTags(existingWedu);
        Set<HashTag> hashTagSet = hashTagService.createHashTags(existingWedu, weduUpdateDto.getHashTags());
        existingWedu.setHashTags(hashTagSet);
        return weduRepository.save(existingWedu);
    }

    public void delete(Long id, String username) {
        Member member = findMember(username);
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));

        if(member != wedu.getHost()) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        Image image = wedu.getImage();
        if(image != null) {
            imageRepository.delete(image);
        }

        memberWeduRepository.deleteAllByWedu(wedu);
        hashTagService.deleteHashTags(wedu);
        challengeService.deleteAllChallengeImages(wedu);
        invitationService.deleteInvitation(wedu);
        weduRepository.delete(wedu);
    }

    public void enroll(Long id, String username) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = findMember(username);
        if(memberWeduRepository.existsByMemberAndWedu(member, wedu)) {
            throw new CustomException(ExceptionType.ALREADY_ENROLLED_WEDU);
        }
        memberWeduRepository.save(MemberWedu.builder().member(member).wedu(wedu).build());
    }

    public void dropout(Long id, String username) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = findMember(username);

        challengeService.deleteByWeduAndMember(wedu, member);
        memberWeduRepository.deleteByWeduAndMember(wedu, member);
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
    }

    public Invitation makeInvitation(Long id, MultipartFile file) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Image image = s3Service.uploadImage(file);
        imageRepository.save(image);
        Invitation invitation = Invitation.builder()
                .wedu(wedu)
                .image(image)
                .build();
        return invitationRepository.save(invitation);
    }

    public WeduAndInviDto getInvitation(Long id) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Invitation invitation = invitationRepository.findByWedu(wedu);
        List<String> hashTags = hashTagService.readHashTags(wedu);
        return WeduAndInviDto.builder()
                .challenge(wedu.getChallenge())
                .invitation(invitation)
                .hashTags(hashTags)
                .build();
    }

    public void createChallengeImage(Long id, ChallengeSaveDto challengeSaveDto, String username) {
        Member member = findMember(username);
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        List<Image> proofImages = new ArrayList<>();
        for(MultipartFile file : challengeSaveDto.getFiles()) {
            Image proofImage = s3Service.uploadImage(file);
            proofImages.add(imageRepository.save(proofImage));
        }

        challengeService.createChallengeImages(member, wedu, proofImages);

    }

    public ChallengeReadDto readChallengeImages(Long id, String nickname, String date) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);

        return challengeService.readChallengeImages(wedu, member, formattedDate);
    }

    public ChallengeMemberList readChallengeMembers(Long id, String date) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return challengeService.readChallengeMembers(wedu, formattedDate);
    }

    public WeduMonthlyProgressDto readMonthlyProgress(Long id, String date) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        List<Long> monthlyProgress = challengeService.readMonthlyProgress(wedu, formattedDate);
        return new WeduMonthlyProgressDto(monthlyProgress, wedu.getCreatedTime().toLocalDate(), wedu.getTargetDate());
    }

    public List<WeduDto> getInWedus(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        List<Wedu> allInWeduList = memberWeduRepository.findAllByMember(member).stream().map(MemberWedu::getWedu).toList();
        List<WeduDto> inWeduDtoList = new ArrayList<>();
        for(Wedu wedu : allInWeduList) {
            if (!wedu.getTargetDate().isBefore(LocalDate.now())) {
                int total = memberWeduRepository.countAllByWedu(wedu);
                Long progress = challengeService.getTodayProgress(wedu, total);
                inWeduDtoList.add(new WeduDto(wedu, total, progress));
            }
        }
        return inWeduDtoList;
    }


}
