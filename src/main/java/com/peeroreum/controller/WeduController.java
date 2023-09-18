package com.peeroreum.controller;

import com.peeroreum.domain.Wedu;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.service.WeduService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class WeduController {

    private final WeduService weduService;

    public WeduController (WeduService weduService) {
        this.weduService = weduService;
    }

    @GetMapping("/wedu")
    public ResponseDto getAllWedus() {
        return ResponseDto.success(weduService.getAllWedus());
    }

    @GetMapping("/wedu/{id}")
    public ResponseDto getWeduById(@PathVariable Long id) {
        return ResponseDto.success(weduService.getWeduById(id));
    }

    @PostMapping("/wedu")
    public ResponseDto createWedu(@RequestBody WeduSaveDto weduSaveDto) {
        return ResponseDto.success(weduService.makeWedu(weduSaveDto));
    }

    @PutMapping("/wedu/{id}")
    public ResponseDto updateWedu(@PathVariable Long id, @RequestBody WeduUpdateDto weduUpdateDto) {
        return ResponseDto.success(weduService.updateWedu(id, weduUpdateDto));
    }

    @DeleteMapping("/wedu/{id}")
    public ResponseDto deleteWedu(@PathVariable Long id) {
        weduService.deleteWedu(id);
        return ResponseDto.success();
    }

}
