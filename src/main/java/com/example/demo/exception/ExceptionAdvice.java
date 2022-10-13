package com.example.demo.exception;

import com.example.demo.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import static com.example.demo.exception.ExceptionType.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {
    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto exception() { return getFailureResponse(EXCEPTION);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDto accessDeniedException() {
        return getFailureResponse(ACCESS_DENIED_EXCEPTION);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto bindException(BindException e) {
        return getFailureResponse(BIND_EXCEPTION, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto loginFailureException() {
        return getFailureResponse(LOGIN_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDto memberEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return getFailureResponse(EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDto memberNicknameAlreadyExistsException(NicknameAlreadyExistsException e) {
        return getFailureResponse(NICKNAME_ALREADY_EXISTS_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto memberNotFoundException() {
        return getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(QuestionNoFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto questionNotFoundException() {
        return getFailureResponse(QUESTION_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(AnswerNotFountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto answerNotFoundException() {
        return getFailureResponse(ANSWER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto imageNotFoundException() {
        return getFailureResponse(IMAGE_NOT_FOUND_EXCEPTION);
    }

    private ResponseDto getFailureResponse(ExceptionType exceptionType) {
        return responseHandler.getFailureResponse(exceptionType);
    }

    private ResponseDto getFailureResponse(ExceptionType exceptionType, Object... args) {
        return responseHandler.getFailureResponse(exceptionType, args);
    }

}
