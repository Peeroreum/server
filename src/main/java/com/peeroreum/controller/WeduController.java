package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.dto.wedu.InvitationDto;
import com.peeroreum.dto.wedu.ChallengeSaveDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.dto.wedu.WeduUpdateDto;
import com.peeroreum.service.WeduService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class WeduController {

    private final WeduService weduService;

    public WeduController (WeduService weduService) {
        this.weduService = weduService;
    }

//    @GetMapping("/wedu")
//    public ResponseDto getAllWedus() {
//        return ResponseDto.success(weduService.getAll());
//    }

    @GetMapping("/wedu/{id}")
    public ResponseDto getWeduById(@PathVariable Long id) {
        return ResponseDto.success(weduService.getById(id));
    }

    @PostMapping("/wedu")
    public ResponseDto createWedu(@ModelAttribute WeduSaveDto weduSaveDto, Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.make(weduSaveDto, username));
    }

    @PutMapping("/wedu/{id}")
    public ResponseDto updateWedu(@PathVariable Long id, @ModelAttribute WeduUpdateDto weduUpdateDto, Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.update(id, weduUpdateDto, username));
    }

    @DeleteMapping("/wedu/{id}")
    public ResponseDto deleteWedu(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        weduService.delete(id, username);
        return ResponseDto.success();
    }

    @PostMapping("/wedu/{id}/enroll")
    public ResponseDto enrollWedu(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        weduService.enroll(id, username);
        return ResponseDto.success();
    }

    @GetMapping("/wedu/my")
    public ResponseDto getMyWedus(Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.getAllMy(username));
    }

    @PostMapping("/wedu/{id}/invitation")
    public ResponseDto createInvitation(@PathVariable Long id, @ModelAttribute InvitationDto invitationDto) {
        return ResponseDto.success(weduService.makeInvitation(id, invitationDto));
    }

    @PutMapping("/wedu/invitation/{id}")
    public ResponseDto updateInvitation(@PathVariable Long id, @ModelAttribute InvitationDto invitationDto) {
        return ResponseDto.success(weduService.updateInvitation(id, invitationDto));
    }

    @GetMapping("/wedu/{id}/invitation")
    public ResponseDto getInvitation(@PathVariable Long id) {
        return ResponseDto.success(weduService.getInvitation(id));
    }

    @PostMapping("/wedu/{id}/challenge")
    public ResponseDto uploadChallengeImage(@PathVariable Long id, @ModelAttribute ChallengeSaveDto challengeSaveDto, Principal principal) {
        String username = principal.getName();
        weduService.createChallengeImage(id, challengeSaveDto, username);
        return ResponseDto.success();
    }

    @GetMapping("/wedu/{id}/challenge/{nickname}/{date}")
    public ResponseDto getChallengeImage(@PathVariable Long id, @PathVariable String nickname, @PathVariable String date) {
        return ResponseDto.success(weduService.readChallengeImages(id, nickname, date));
    }
}
