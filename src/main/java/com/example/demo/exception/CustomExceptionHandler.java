package com.example.demo.exception;

import com.example.demo.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handleException(CustomException e) {
        return ResponseEntity.status(e.exceptionType.getHttpStatus()).body(ResponseDto.fail(e.exceptionType.getHttpStatus().value(), e.exceptionType.getMessage()));
    }
}
