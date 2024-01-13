package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.dto.wedu.ChallengeSaveDto;
import com.peeroreum.dto.wedu.WeduSaveDto;
import com.peeroreum.dto.wedu.WeduUpdateDto;
import com.peeroreum.service.WeduService;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;

@RestController
public class WeduController {

    private final WeduService weduService;

    public WeduController (WeduService weduService) {
        this.weduService = weduService;
    }

    @GetMapping("/wedu")
    public ResponseDto getAllWedus(@RequestParam("sort") String sort, @RequestParam("grade") Long grade, @RequestParam("subject") Long subject, Principal principal) {
        String username = principal.getName();
        if(sort.equals("추천순")) {
            return ResponseDto.success(weduService.getAllRecommends(username));
        } else if(sort.equals("인기순")) {
            return ResponseDto.success(weduService.getAllPopular(grade, subject));
        } else {
            return ResponseDto.success(weduService.getAll(grade, subject));
        }
    }

    @GetMapping("/wedu/search/{keyword}")
    public ResponseDto searchWedus(@PathVariable String keyword) {
        return ResponseDto.success(weduService.getSearchResults(keyword));
    }

    @GetMapping("/wedu/{id}")
    public ResponseDto getWeduById(@PathVariable Long id, Principal principal) {
        return ResponseDto.success(weduService.getById(id, principal.getName()));
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

    @DeleteMapping("/wedu/{id}/out")
    public ResponseDto dropoutWedu(@PathVariable Long id, Principal principal) {
        weduService.dropout(id, principal.getName());
        return ResponseDto.success();
    }

    @GetMapping("/wedu/my")
    public ResponseDto getMyWedus(Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(weduService.getAllMy(username));
    }

    @GetMapping("/wedu/in")
    public ResponseDto getInWedus(@PathParam("nickname") String nickname) {
        return ResponseDto.success(weduService.getInWedus(nickname));
    }

//    @PostMapping("/wedu/{id}/invitation")
//    public ResponseDto createInvitation(@PathVariable Long id, @ModelAttribute InvitationDto invitationDto) {
//        return ResponseDto.success(weduService.makeInvitation(id, invitationDto));
//    }

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

    @DeleteMapping("/wedu/{id}/challenge")
    public ResponseDto deleteChallengeImage(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        weduService.deleteChallengeImages(id, username);
        return ResponseDto.success();
    }

    @GetMapping("/wedu/{id}/challenge/{nickname}/{date}")
    public ResponseDto getChallengeImage(@PathVariable Long id, @PathVariable String nickname, @PathVariable String date) {
        return ResponseDto.success(weduService.readChallengeImages(id, nickname, date));
    }

    @GetMapping("/wedu/{id}/challenge/{date}")
    public ResponseDto getChallengeMembers(@PathVariable Long id, @PathVariable String date) {
        return ResponseDto.success(weduService.readChallengeMembers(id, date));
    }

    @GetMapping("/wedu/{id}/monthly/{date}")
    public ResponseDto getMonthlyProgress(@PathVariable Long id, @PathVariable String date) {
        return ResponseDto.success(weduService.readMonthlyProgress(id, date));
    }
}
