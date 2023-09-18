package com.peeroreum.service;

import com.peeroreum.domain.Wedu;
import com.peeroreum.dto.wedu.WeduDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.repository.WeduRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeduService {

    private final WeduRepository weduRepository;

    public WeduService (WeduRepository weduRepository) {
        this.weduRepository = weduRepository;
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

    public Wedu makeWedu(WeduSaveDto weduSaveDto) {
        Wedu savedWedu = WeduSaveDto.toEntity(weduSaveDto);
        return weduRepository.save(savedWedu);
    }

    public Wedu updateWedu(Long id, Wedu updatedWedu) {
        Optional<Wedu> existingWedu = weduRepository.findById(id);
        if(existingWedu.isPresent()) {
            Wedu weduToUpdate = existingWedu.get();
            weduToUpdate.setTitle(updatedWedu.getTitle());
            weduToUpdate.setImage(updatedWedu.getImage());
            weduToUpdate.setMaximumPeople(updatedWedu.getMaximumPeople());
            weduToUpdate.setIsSearchable(updatedWedu.isSearchable());
            weduToUpdate.setIsLocked(updatedWedu.isLocked());
            weduToUpdate.setPassword(updatedWedu.getPassword());
            weduToUpdate.setGrade(updatedWedu.getGrade());
            weduToUpdate.setSubject(updatedWedu.getSubject());
            weduToUpdate.setGender(updatedWedu.getGender());

            return weduRepository.save(weduToUpdate);
        } else {
            throw new EntityNotFoundException("Wedu not found with id: " + id);
        }
    }

    public void deleteWedu(Long id) {
        weduRepository.deleteById(id);
    }

}
