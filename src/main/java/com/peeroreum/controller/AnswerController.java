package com.peeroreum.controller;

import com.peeroreum.dto.answer.AnswerReadRequest;
import com.peeroreum.dto.answer.AnswerSaveDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.AnswerService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/answer")
    public ResponseDto createAnswer(@ModelAttribute AnswerSaveDto answerSaveDto, Principal principal) {
        return ResponseDto.success(answerService.create(answerSaveDto, principal.getName()));
    }

    @PostMapping("/answer/read")
    public ResponseDto readAllAnswer(@RequestBody AnswerReadRequest answerReadRequest, @RequestParam(defaultValue = "0") int page, Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(answerService.readAll(answerReadRequest, page, username));
    }

    @DeleteMapping("/answer/{id}")
    public ResponseDto deleteAnswer(@PathVariable Long id) {
        answerService.delete(id);
        return ResponseDto.success();
    }
}
