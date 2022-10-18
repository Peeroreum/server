package com.example.demo.controller;

import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/question/{questionId}/like")
    public ResponseDto likeQuestion(@PathVariable Long questionId) {
        heartService.likeQuestion(questionId);
        return ResponseDto.success();
    }

    @PostMapping("/question/{questionId}/dislike")
    public ResponseDto dislikeQuestion(@PathVariable Long questionId) {
        heartService.dislikeQuestion(questionId);
        return ResponseDto.success();
    }


    @DeleteMapping("/question/{questionId}/like")
    public ResponseDto cancelQuestionLike(@PathVariable Long questionId) {
        heartService.cancelQuestionLike(questionId);
        return ResponseDto.success();
    }

    @DeleteMapping("/question/{questionId}/dislike")
    public ResponseDto cancelQuestionDislike(@PathVariable Long questionId) {
        heartService.cancelQuestionDislike(questionId);
        return ResponseDto.success();
    }


    @PostMapping("/answer/{answerId}/like")
    public ResponseDto likeAnswer(@PathVariable Long answerId) {
        heartService.likeAnswer(answerId);
        return ResponseDto.success();
    }

    @PostMapping("/answer/{answerId}/dislike")
    public ResponseDto dislikeAnswer(@PathVariable Long answerId) {
        heartService.dislikeAnswer(answerId);
        return ResponseDto.success();
    }

    @DeleteMapping("/answer/{answerId}/like")
    public ResponseDto cancelAnswerLike(@PathVariable Long answerId) {
        heartService.cancelAnswerLike(answerId);
        return ResponseDto.success();
    }

    @DeleteMapping("/answer/{answerId}/dislike")
    public ResponseDto cancelAnswerDislike(@PathVariable Long answerId) {
        heartService.cancelAnsweDislike(answerId);
        return ResponseDto.success();
    }
}
