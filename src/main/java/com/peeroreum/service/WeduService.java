package com.peeroreum.service;

import com.peeroreum.domain.Wedu;
import com.peeroreum.dto.wedu.WeduDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.repository.WeduRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeduService {

    private final WeduRepository weduRepository;

    public WeduService (WeduRepository weduRepository) {
        this.weduRepository = weduRepository;
    }

    public List<WeduDto> findAllWedu() {
        List<Wedu> weduList = weduRepository.findAll();
        List<WeduDto> weduDtoList = new ArrayList<>();
        int attendingPeopleNum = 0, dDay = 0;
        for(Wedu wedu : weduList) {
            weduDtoList.add(new WeduDto(wedu, attendingPeopleNum, dDay));
        }

        return weduDtoList;
    }


    public String makeWedu(WeduSaveDto weduSaveDto) {
        Wedu savedWedu = WeduSaveDto.toEntity(weduSaveDto);
        if(savedWedu == null) {

        }
        return "success to make Wedu Room";
    }
}
