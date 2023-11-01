package com.peeroreum.controller;

import com.peeroreum.dto.question.QuestionSaveDto;
import com.peeroreum.dto.question.QuestionSearchRequest;
import com.peeroreum.dto.question.QuestionUpdateDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("home/question")
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
    public ResponseDto readQuestion(@PathVariable Long id, Principal principal){
        String username = principal.getName();
        return ResponseDto.success(questionService.read(id, username));
    }

    @PutMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updateQuestion(@PathVariable Long id, @ModelAttribute QuestionUpdateDto questionUpdateDto) {
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