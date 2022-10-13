package com.example.demo.controller;

import com.example.demo.dto.question.QuestionSaveDto;
import com.example.demo.dto.question.QuestionUpdateDto;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/question")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto createQuestion(@RequestBody QuestionSaveDto questionSaveDto, Principal principal) throws IOException {
        String username = principal.getName();
        questionService.create(questionSaveDto, username);
        return ResponseDto.success();
    }

    @GetMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto readQuestion(@PathVariable Long id){
        return ResponseDto.success(questionService.read(id));
    }

    @PostMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateDto questionUpdateDto) throws IOException {
        questionService.update(id, questionUpdateDto);
        return ResponseDto.success();
    }

    @DeleteMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteQuestion(@PathVariable Long id){
        questionService.delete(id);
        return ResponseDto.success();
    }
}
