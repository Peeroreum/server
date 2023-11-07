package com.peeroreum.service;

import com.peeroreum.domain.Image;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.MemberWedu;
import com.peeroreum.domain.Wedu;
import com.peeroreum.dto.wedu.WeduDto;
import com.peeroreum.dto.wedu.WeduReadDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.dto.wedu.WeduUpdateDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.ImageRepository;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.repository.MemberWeduRepository;
import com.peeroreum.repository.WeduRepository;
import com.peeroreum.service.attachment.S3Service;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeduService {

    private final WeduRepository weduRepository;
    private final MemberRepository memberRepository;
    private final MemberWeduRepository memberWeduRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public WeduService (WeduRepository weduRepository, MemberRepository memberRepository, MemberWeduRepository memberWeduRepository, ImageRepository imageRepository, S3Service s3Service) {
        this.weduRepository = weduRepository;
        this.memberRepository = memberRepository;
        this.memberWeduRepository = memberWeduRepository;
        this.imageRepository = imageRepository;
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
        Wedu savingWedu = Wedu.builder()
                .title(weduSaveDto.getTitle())
                .image(image)
                .host(host)
                .maximumPeople(weduSaveDto.getMaximumPeople())
                .isLocked(weduSaveDto.isLocked())
                .password(weduSaveDto.getPassword())
                .grade(weduSaveDto.getGrade())
                .subject(weduSaveDto.getSubject())
                .gender(weduSaveDto.getGender())
                .targetDate(weduSaveDto.getTargetDate())
                .build();
        Wedu wedu = weduRepository.save(savingWedu);
        memberWeduRepository.save(MemberWedu.builder().member(host).wedu(wedu).build());
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
        weduRepository.delete(existingWedu);
    }

    public void enroll(Long id, String username) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = findMember(username);
        if(memberWeduRepository.existsByMember(member)) {
            throw new CustomException(ExceptionType.ALREADY_ENROLLED_WEDU);
        }
        memberWeduRepository.save(MemberWedu.builder().member(member).wedu(wedu).build());
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
    }

}
