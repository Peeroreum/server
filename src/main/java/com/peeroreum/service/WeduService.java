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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final S3Service s3Service;

    public WeduService (WeduRepository weduRepository, MemberRepository memberRepository, MemberWeduRepository memberWeduRepository, InvitationRepository invitationRepository, ImageRepository imageRepository, HashTagService hashTagService, ChallengeService challengeService, S3Service s3Service) {
        this.weduRepository = weduRepository;
        this.memberRepository = memberRepository;
        this.memberWeduRepository = memberWeduRepository;
        this.invitationRepository = invitationRepository;
        this.imageRepository = imageRepository;
        this.hashTagService = hashTagService;
        this.challengeService = challengeService;
        this.s3Service = s3Service;
    }

//    public List<WeduDto> getAll() {
//        List<Wedu> weduList = weduRepository.findAll();
//        List<WeduDto> weduDtoList = new ArrayList<>();
//        int dDay = 0;
//        for(Wedu wedu : weduList) {
//            weduDtoList.add(new WeduDto(wedu, dDay));
//        }
//
//        return weduDtoList;
//    }

    public List<WeduDto> getAllMy(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        List<MemberWedu> myWeduList = memberWeduRepository.findAllByMember(member);
        List<WeduDto> myWeduDtoList = new ArrayList<>();
        for(MemberWedu memberWedu : myWeduList) {
            Wedu myWedu = memberWedu.getWedu();
            myWeduDtoList.add(new WeduDto(myWedu, memberWeduRepository.countAllByWedu(myWedu)));
        }
        return myWeduDtoList;
    }

    public WeduReadDto getById(Long id) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        WeduReadDto weduReadDto = WeduReadDto.builder()
                .title(wedu.getTitle())
                .imageUrl(wedu.getImage().getImagePath())
                .dDay(LocalDate.now().until(wedu.getTargetDate(), ChronoUnit.DAYS))
                .challenge(wedu.getChallenge())
                .build();
        return weduReadDto;
    }

    public Wedu make(WeduSaveDto weduSaveDto, String username) {
        Member host = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Image image = imageRepository.save(s3Service.uploadImage(weduSaveDto.getFile()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Wedu savingWedu = Wedu.builder()
                .title(weduSaveDto.getTitle())
                .image(image)
                .host(host)
                .maximumPeople(weduSaveDto.getMaximumPeople())
                .isLocked((weduSaveDto.getIsLocked() == 1)? true : false)
                .password(weduSaveDto.getPassword())
                .grade(weduSaveDto.getGrade())
                .subject(weduSaveDto.getSubject())
                .gender(weduSaveDto.getGender())
                .challenge(weduSaveDto.getChallenge())
                .targetDate(LocalDate.parse(weduSaveDto.getTargetDate(), formatter))
                .build();
        Wedu wedu = weduRepository.save(savingWedu);
        memberWeduRepository.save(MemberWedu.builder().member(host).wedu(wedu).build());
        hashTagService.createHashTags(wedu, weduSaveDto.getHashTags());
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
        existingWedu.update(image, weduUpdateDto.getMaximumPeople(), weduUpdateDto.getGender(), weduUpdateDto.isLocked(), weduUpdateDto.getPassword());
        hashTagService.deleteHashTags(existingWedu);
        hashTagService.createHashTags(existingWedu, weduUpdateDto.getHashTags());

        return weduRepository.save(existingWedu);
    }

    public void delete(Long id, String username) {
        Member member = findMember(username);
        Wedu existingWedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));

        if(member != existingWedu.getHost()) {
            throw new CustomException(ExceptionType.DO_NOT_HAVE_PERMISSION);
        }

        Image image = existingWedu.getImage();
        s3Service.deleteImage(image.getImageName());
        imageRepository.delete(image);

        memberWeduRepository.deleteAllByWedu(existingWedu);
        hashTagService.deleteHashTags(existingWedu);
        weduRepository.delete(existingWedu);
    }

    public void enroll(Long id, String username) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = findMember(username);
        if(memberWeduRepository.existsByMemberAndWedu(member, wedu)) {
            throw new CustomException(ExceptionType.ALREADY_ENROLLED_WEDU);
        }
        memberWeduRepository.save(MemberWedu.builder().member(member).wedu(wedu).build());
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
    }

    public Invitation makeInvitation(Long id, InvitationDto invitationDto) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Invitation invitation = Invitation.builder()
                .backgroundColor(invitationDto.getBackgroundColor())
                .content(invitationDto.getContent())
                .textColor(invitationDto.getTextColor())
                .wedu(wedu)
                .build();
        return invitationRepository.save(invitation);
    }

    public Invitation updateInvitation(Long id, InvitationDto invitationDto) {
        Invitation invitation = invitationRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.INVITATION_NOT_FOUND_EXCEPTION));
        invitation.update(invitationDto.getContent(), invitationDto.getBackgroundColor(), invitationDto.getTextColor());
        return invitationRepository.save(invitation);
    }

    public InvitationDto getInvitation(Long id) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Invitation invitation = invitationRepository.findByWedu(wedu);
        return new InvitationDto(invitation.getContent(), invitation.getBackgroundColor(), invitation.getTextColor());
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
        List<MemberWedu> memberWedus = memberWeduRepository.findAllByWedu(wedu);
        List<Member> allMembers = memberWedus.stream().map(MemberWedu::getMember).collect(Collectors.toList());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return challengeService.readChallengeMembers(allMembers, wedu, formattedDate);
    }

    public MonthlyProgress readMonthlyProgress(Long id, String date) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        int total = memberWeduRepository.countAllByWedu(wedu);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return challengeService.readMonthlyProgress(total, formattedDate);
    }
}
