package com.example.demo.exception;

import com.example.demo.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final MessageSource messageSource;

    public ResponseDto getFailureResponse(ExceptionType exceptionType) {
        return ResponseDto.fail(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage()));
    }

    public ResponseDto getFailureResponse(ExceptionType exceptionType, Object... args) {
        return ResponseDto.fail(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage(), args));
    }

    private Integer getCode(String key) {
        return Integer.valueOf(messageSource.getMessage(key, null, LocaleContextHolder.getLocale()));
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
