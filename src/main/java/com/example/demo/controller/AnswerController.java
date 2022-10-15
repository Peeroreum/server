package com.example.demo.controller;

import com.example.demo.dto.answer.AnswerReadRequest;
import com.example.demo.dto.answer.AnswerSaveDto;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto createAnswer(@ModelAttribute AnswerSaveDto answerSaveDto, Principal principal) throws IOException {
        String username = principal.getName();
        answerService.create(answerSaveDto, username);
        return ResponseDto.success();
    }

    @GetMapping("/answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto readAllAnswer(@RequestBody AnswerReadRequest answerReadRequest) {
        return ResponseDto.success(answerService.readAll(answerReadRequest));
    }

    @DeleteMapping("/answer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteAnswer(@PathVariable Long id) {
        answerService.delete(id);
        return ResponseDto.success();
    }
}