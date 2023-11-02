package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/question/{questionId}/like")
    public ResponseDto likeQuestion(@PathVariable Long questionId, Principal principal) {
        String username = principal.getName();
        heartService.likeQuestion(questionId, username);
        return ResponseDto.success();
    }

    @DeleteMapping("/question/{questionId}/like")
    public ResponseDto cancelQuestionLike(@PathVariable Long questionId, Principal principal) {
        String username = principal.getName();
        heartService.cancelQuestionLike(questionId, username);
        return ResponseDto.success();
    }

    @PostMapping("/answer/{answerId}/like")
    public ResponseDto likeAnswer(@PathVariable Long answerId, Principal principal) {
        String username = principal.getName();
        heartService.likeAnswer(answerId, username);
        return ResponseDto.success();
    }

    @DeleteMapping("/answer/{answerId}/like")
    public ResponseDto cancelAnswerLike(@PathVariable Long answerId, Principal principal) {
        String username = principal.getName();
        heartService.cancelAnswerLike(answerId, username);
        return ResponseDto.success();
    }

}
