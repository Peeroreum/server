package com.peeroreum.controller;

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

    @GetMapping("/answer")
    public ResponseDto readAllAnswer(@RequestParam("questionId") Long questionId, @RequestParam(defaultValue = "0") int page, Principal principal) {
        return ResponseDto.success(answerService.readAll(questionId, page, principal.getName()));
    }

    @DeleteMapping("/answer/{id}")
    public ResponseDto deleteAnswer(@PathVariable Long id, Principal principal) {
        answerService.delete(id, principal.getName());
        return ResponseDto.success();
    }

    @PutMapping("/answer/{id}/select")
    public ResponseDto selectAnswer(@PathVariable Long id, Principal principal) {
        answerService.selectAnswer(id, principal.getName());
        return ResponseDto.success();
    }
}
