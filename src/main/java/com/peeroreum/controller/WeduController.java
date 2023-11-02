package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.dto.wedu.WeduUpdateDto;
import com.peeroreum.service.WeduService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public ResponseDto createWedu(@ModelAttribute WeduSaveDto weduSaveDto, Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.make(weduSaveDto, username));
    }

    @PutMapping("/wedu/{id}")
    public ResponseDto updateWedu(@PathVariable Long id, @ModelAttribute WeduUpdateDto weduUpdateDto, Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.updateWedu(id, weduUpdateDto, username));
    }

    @DeleteMapping("/wedu/{id}")
    public ResponseDto deleteWedu(@PathVariable Long id) {
        weduService.deleteWedu(id);
        return ResponseDto.success();
    }

    @PostMapping("/wedu/{id}/enroll")
    public ResponseDto enrollWedu(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        weduService.enrollWedu(id, username);
        return ResponseDto.success();
    }

    @GetMapping("/wedu/my")
    public ResponseDto getMyWedus(Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.getAllMyWedus(username));
    }
}
