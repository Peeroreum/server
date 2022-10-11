package com.example.demo.controller;

import com.example.demo.dto.question.QuestionSaveDto;
import com.example.demo.dto.question.QuestionUpdateDto;
import com.example.demo.dto.response.Response;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/question")
    @ResponseStatus(HttpStatus.OK)
    public Response createQuestion(@RequestBody QuestionSaveDto questionSaveDto){
        questionService.create(questionSaveDto);
        return Response.success();
    }

    @GetMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response readQuestion(@PathVariable Long id){
        return Response.success(questionService.read(id));
    }

    @PostMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateDto questionUpdateDto) {
        questionService.update(id, questionUpdateDto);
        return Response.success();
    }

    @DeleteMapping("/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteQuestion(@PathVariable Long id){
        questionService.delete(id);
        return Response.success();
    }
}
