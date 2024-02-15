package com.peeroreum.controller;

import com.peeroreum.dto.question.QuestionSaveDto;
import com.peeroreum.dto.question.QuestionUpdateDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/question")
    public ResponseDto getQuestions(@RequestParam("grade") Long grade, @RequestParam("subject") Long subject, @RequestParam("detailSubject") Long detailSubject, @RequestParam(defaultValue = "0") int page) {
        return ResponseDto.success(questionService.getQuestions(grade, subject, detailSubject, page));
    }

    @GetMapping("/question/search/{keyword}")
    public ResponseDto searchQuestions(@PathVariable String keyword) {
        return ResponseDto.success(questionService.getSearchResults(keyword));
    }

    @PostMapping("/question")
    public ResponseDto createQuestion(@ModelAttribute QuestionSaveDto saveDto, Principal principal) {
        String username = principal.getName();
        questionService.create(saveDto, username);
        return ResponseDto.success();
    }

    @GetMapping("/question/{id}")
    public ResponseDto readQuestion(@PathVariable Long id, Principal principal){
        String username = principal.getName();
        return ResponseDto.success(questionService.read(id, username));
    }

    @PutMapping("/question/{id}")
    public ResponseDto updateQuestion(@PathVariable Long id, @ModelAttribute QuestionUpdateDto questionUpdateDto) {
        questionService.update(id, questionUpdateDto);
        return ResponseDto.success();
    }

    @DeleteMapping("/question/{id}")
    public ResponseDto deleteQuestion(@PathVariable Long id){
        questionService.delete(id);
        return ResponseDto.success();
    }
}
