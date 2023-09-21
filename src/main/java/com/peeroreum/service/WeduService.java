package com.peeroreum.service;

import com.peeroreum.domain.Image;
import com.peeroreum.domain.Member;
import com.peeroreum.domain.Wedu;
import com.peeroreum.dto.wedu.WeduDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.dto.wedu.WeduUpdateDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.repository.WeduRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeduService {

    private final WeduRepository weduRepository;
    private final MemberRepository memberRepository;

    public WeduService (WeduRepository weduRepository, MemberRepository memberRepository) {
        this.weduRepository = weduRepository;
        this.memberRepository = memberRepository;
    }

    public List<WeduDto> getAllWedus() {
        List<Wedu> weduList = weduRepository.findAll();
        List<WeduDto> weduDtoList = new ArrayList<>();
        int dDay = 0;
        for(Wedu wedu : weduList) {
            weduDtoList.add(new WeduDto(wedu, dDay));
        }

        return weduDtoList;
    }

    public Optional<Wedu> getWeduById(Long id) {
        return weduRepository.findById(id);
    }

    public Wedu makeWedu(WeduSaveDto weduSaveDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Wedu savedWedu = WeduSaveDto.toEntity(weduSaveDto);
        return weduRepository.save(savedWedu);
    }

    public Wedu updateWedu(Long id, WeduUpdateDto weduUpdateDto, String username) {
        Wedu existingWedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        existingWedu.setTitle(weduUpdateDto.getTitle());
//        existingWedu.setImage(weduUpdateDto.getImage()); 이미지 s3에 저장 과정 거친 후 DB 등록하도록 수정 필요
        existingWedu.setMaximumPeople(weduUpdateDto.getMaximumPeople());
        existingWedu.setPassword(weduUpdateDto.getPassword());
        existingWedu.setGrade(weduUpdateDto.getGrade());
        existingWedu.setSubject(weduUpdateDto.getSubject());
        existingWedu.setGender(weduUpdateDto.getGender());

        return weduRepository.save(existingWedu);
    }
    public void deleteWedu(Long id) {
        weduRepository.deleteById(id);
    }

    public void enrollWedu(Long id, String username) {
        Wedu wedu = weduRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionType.WEDU_NOT_FOUND_EXCEPTION));
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        member.setWedus(wedu);
        wedu.setMember(member);
    }

}
