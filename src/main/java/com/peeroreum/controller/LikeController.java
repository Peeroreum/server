package com.peeroreum.controller;

import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.LikeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/question/{id}")
    public ResponseDto createQuestionLike(@PathVariable Long id, Principal principal) {
        likeService.makeQuestionLike(id, principal.getName());
        return ResponseDto.success();
    }

    @DeleteMapping("/like/question/{id}")
    public ResponseDto deleteQuestionLike(@PathVariable Long id, Principal principal) {
        likeService.cancelQuestionLike(id, principal.getName());
        return ResponseDto.success();
    }

    @PostMapping("/like/answer/{id}")
    public ResponseDto createAnswerLike(@PathVariable Long id, Principal principal) {
        likeService.makeAnswerLike(id, principal.getName());
        return ResponseDto.success();
    }

    @DeleteMapping("/like/answer/{id}")
    public ResponseDto deleteAnswerLike(@PathVariable Long id, Principal principal) {
        likeService.cancelAnswerLike(id, principal.getName());
        return ResponseDto.success();
    }
}
