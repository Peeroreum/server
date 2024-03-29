package com.peeroreum.service;

import com.peeroreum.domain.*;
import com.peeroreum.domain.image.ChallengeImage;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.wedu.*;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.*;
import com.peeroreum.service.attachment.S3Service;
import org.springframework.data.domain.PageRequest;
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

    public List<WeduDto> getAll(Long grade, Long subject, int page) {
        List<Wedu> weduList = getWedus(grade, subject, page);
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

    public List<WeduDto> getAllRecommends(String username, int page) {
        Member member = findMember(username);
        List<Wedu> weduList = getWedus(member.getGrade(), member.getGoodSubject(), page);
        weduList.addAll(getWedus(member.getGrade(), member.getBadSubject(), page));
        return getWeduDtos(weduList);
    }

    public List<WeduDto> getAllPopular(Long grade, Long subject, int page) {
        List<Wedu> weduList = getWedus(grade, subject, page);
        List<WeduDto> weduDtoList = getWeduDtos(weduList);
        weduDtoList.sort(Comparator.comparing(WeduDto::getAttendingPeopleNum).reversed());
        return weduDtoList;
    }

    private List<Wedu> getWedus(Long grade, Long subject, int page) {
        List<Wedu> weduList;
        if(grade == 0 && subject == 0) {
            weduList = weduRepository.findAllByOrderByIdDesc(PageRequest.of(page, 15));
        } else if(grade == 0) {
            weduList = weduRepository.findAllBySubjectOrderByIdDesc(subject, PageRequest.of(page, 15));
        } else if(subject == 0) {
            weduList = weduRepository.findAllByGradeOrderByIdDesc(grade, PageRequest.of(page, 15));
        } else {
            weduList = weduRepository.findAllByGradeAndSubjectOrderByIdDesc(grade, subject, PageRequest.of(page, 15));
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
        int total = memberWeduRepository.countAllByWedu(wedu);
        Long progress = challengeService.getTodayProgress(wedu, total);
        List<String> hashTags = hashTagService.readHashTags(wedu);
        int attendingPeopleNum = memberWeduRepository.countAllByWedu(wedu);

        return WeduReadDto.builder()
                .title(wedu.getTitle())
                .subject(wedu.getSubject())
                .grade(wedu.getGrade())
                .maxPeopleNum(wedu.getMaximumPeople())
                .imageUrl(wedu.getImage() != null ? wedu.getImage().getImagePath() : null)
                .dDay(LocalDate.now().until(wedu.getTargetDate(), ChronoUnit.DAYS))
                .challenge(wedu.getChallenge())
                .continuousDate(challengeService.getContinuousDate(member, wedu))
                .isLocked(wedu.isLocked())
                .password(wedu.getPassword())
                .progress(progress)
                .hostMail(wedu.getHost().getUsername())
                .hashTags(hashTags)
                .attendingPeopleNum(attendingPeopleNum)
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

        if(weduUpdateDto.getImage() != null) {
            Image image = existingWedu.getImage();
            if(image != null) {
                s3Service.deleteImage(image.getImageName());
                imageRepository.delete(image);
            }
            if(weduUpdateDto.getImage().isEmpty()) {
                image = null;
            } else {
                image = imageRepository.save(s3Service.uploadImage(weduUpdateDto.getImage()));
            }
            existingWedu.updateImage(image);
        }

        if(weduUpdateDto.getMaximumPeople() != null) {
            existingWedu.updateMaximum(weduUpdateDto.getMaximumPeople());
        }

        if(weduUpdateDto.getIsLocked() != null) {
            existingWedu.updateLocked(weduUpdateDto.getIsLocked());
            if(weduUpdateDto.getIsLocked()) {
                existingWedu.updatePassword(weduUpdateDto.getPassword());
            } else {
                existingWedu.updatePassword(null);
            }
        }

        if(weduUpdateDto.getHashTags() != null) {
            List<HashTag> hashTags = existingWedu.getHashTags().stream().toList();
            Set<HashTag> newHashTags = hashTagService.createHashTags(existingWedu, weduUpdateDto.getHashTags());

            if(!hashTags.isEmpty()) {
                hashTagService.deleteAllHashTags(hashTags);
            }

            existingWedu.updateHashTags(newHashTags);
        }

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
        challengeService.deleteTodayChallengeImages(wedu);
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

    public void deleteChallengeImages(Long id, String username) {
        Member member = findMember(username);
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        challengeService.deleteTodayChallengeImages(member, wedu);
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
