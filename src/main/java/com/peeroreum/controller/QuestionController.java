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

    @PostMapping("/question")
    public ResponseDto createQuestion(@ModelAttribute QuestionSaveDto saveDto, Principal principal) {
        return ResponseDto.success(questionService.create(saveDto, principal.getName()));
    }

    @GetMapping("/question")
    public ResponseDto getQuestions(@RequestParam(value = "grade", defaultValue = "0") Long grade, @RequestParam(value = "subject", defaultValue = "0") Long subject, @RequestParam(value = "detailSubject", defaultValue = "0") Long detailSubject, @RequestParam(defaultValue = "0") int page) {
        return ResponseDto.success(questionService.getQuestions(grade, subject, detailSubject, page));
    }

    @GetMapping("/question/search/{keyword}")
    public ResponseDto searchQuestions(@PathVariable String keyword, @RequestParam(value = "grade", defaultValue = "0") Long grade, @RequestParam(value = "subject", defaultValue = "0") Long subject, @RequestParam(value = "detailSubject", defaultValue = "0") Long detailSubject, @RequestParam(defaultValue = "0") int page) {
        return ResponseDto.success(questionService.getSearchResults(keyword, grade, subject, detailSubject, page));
    }

    @GetMapping("/question/{id}")
    public ResponseDto readQuestion(@PathVariable Long id, Principal principal){
        return ResponseDto.success(questionService.readById(id, principal.getName()));
    }

    @PutMapping("/question/{id}")
    public ResponseDto updateQuestion(@PathVariable Long id, @ModelAttribute QuestionUpdateDto questionUpdateDto, Principal principal) {
        return ResponseDto.success(questionService.update(id, questionUpdateDto, principal.getName()));
    }

    @DeleteMapping("/question/{id}")
    public ResponseDto deleteQuestion(@PathVariable Long id, Principal principal){
        questionService.delete(id, principal.getName());
        return ResponseDto.success();
    }

    @GetMapping("/question/my")
    public ResponseDto getMyQuestions(Principal principal, @RequestParam(defaultValue = "0") int page) {
        return ResponseDto.success(questionService.getAllMy(principal.getName(), page));
    }
}