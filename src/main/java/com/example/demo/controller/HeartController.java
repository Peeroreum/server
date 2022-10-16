package com.example.demo.controller;

import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/question/{questionId}/like")
    public ResponseDto likeQuestion(@PathVariable Long questionId) {
        heartService.likeQuestion(questionId);
        return ResponseDto.success();
    }

    @DeleteMapping("/question/{questionId}/like")
    public ResponseDto unlikeQuestion(@PathVariable Long questionId) {
        heartService.unlikeQuestion(questionId);
        return ResponseDto.success();
    }

    @PostMapping("/answer/{answerId}/like")
    public ResponseDto likeAnswer(@PathVariable Long answerId) {
        heartService.likeAnswer(answerId);
        return ResponseDto.success();
    }

    @DeleteMapping("/answer/{answerId}/like")
    public ResponseDto unlikeAnswer(@PathVariable Long answerId) {
        heartService.unlikeAnswer(answerId);
        return ResponseDto.success();
    }
}
