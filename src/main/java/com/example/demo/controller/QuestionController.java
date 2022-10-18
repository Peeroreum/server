package com.example.demo.controller;

import com.example.demo.dto.question.QuestionSaveDto;
import com.example.demo.dto.question.QuestionSearchRequest;
import com.example.demo.dto.question.QuestionUpdateDto;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/question")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto searchQuestion(@RequestBody QuestionSearchRequest searchRequest) {
        return ResponseDto.success(questionService.search(searchRequest));
    }

    @PostMapping("/question")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto createQuestion(@ModelAttribute QuestionSaveDto saveDto, Principal principal) {
        String username = principal.getName();
        questionService.create(saveDto, username);
        return ResponseDto.success();
    }

    @GetMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto readQuestion(@PathVariable Long id){
        return ResponseDto.success(questionService.read(id));
    }

    @PutMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateDto questionUpdateDto) {
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
