package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("/ranking")
    public ResponseDto getRanking() {
        return ResponseDto.success(rankingService.getRankingList());
    }

    @GetMapping("/ranking/my")
    public ResponseDto getMyRanking(Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(rankingService.getMyRanking(username));
    }
}
