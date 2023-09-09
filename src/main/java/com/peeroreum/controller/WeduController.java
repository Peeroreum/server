package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.service.WeduService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WeduController {

    private final WeduService weduService;

    public WeduController (WeduService weduService) {
        this.weduService = weduService;
    }

    @GetMapping("/wedu")
    public ResponseDto getWedus() {
        return ResponseDto.success(weduService.findAllWedu());
    }

    @PostMapping("/wedu")
    public ResponseDto postWedu(@RequestBody WeduSaveDto weduSaveDto) {
        return ResponseDto.success(weduService.makeWedu(weduSaveDto));
    }

}
