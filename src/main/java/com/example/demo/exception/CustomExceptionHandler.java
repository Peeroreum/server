package com.example.demo.exception;

import com.example.demo.dto.response.ResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseDto handleException(CustomException e) {
        return ResponseDto.fail(e.exceptionType.getHttpStatus().value(), e.exceptionType.getMessage());
    }
}
