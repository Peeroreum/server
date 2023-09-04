package com.peeroreum.controller;

import com.peeroreum.dto.answer.AnswerReadRequest;
import com.peeroreum.dto.answer.AnswerSaveDto;
import com.peeroreum.dto.answer.AnswerUpdateDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto createAnswer(@ModelAttribute AnswerSaveDto answerSaveDto, Principal principal) {
        String username = principal.getName();
        answerService.create(answerSaveDto, username);
        return ResponseDto.success();
    }

    @PostMapping("/answer/read")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto readAllAnswer(@RequestBody AnswerReadRequest answerReadRequest, Principal principal) {
        String username = principal.getName();
        return ResponseDto.success(answerService.readAll(answerReadRequest, username));
    }

    @PutMapping("/answer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updateAnswer(@PathVariable Long id, @ModelAttribute AnswerUpdateDto answerUpdateDto) {
        answerService.update(id, answerUpdateDto);
        return ResponseDto.success();
    }

    @DeleteMapping("/answer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteAnswer(@PathVariable Long id) {
        answerService.delete(id);
        return ResponseDto.success();
    }
}
