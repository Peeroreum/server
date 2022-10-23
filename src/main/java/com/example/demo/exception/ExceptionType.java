package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    LOGIN_FAILURE_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    USERNAME_ALREADY_EXISTS_EXCEPTION(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    NICKNAME_ALREADY_EXISTS_EXCEPTION(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    MEMBER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    QUESTION_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 질문입니다."),
    ANSWER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 답변입니다."),
    IMAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다."),
    FILETYPE_WRONG_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일입니다."),
    UPLOAD_FAILURE_EXCEPTION(HttpStatus.EXPECTATION_FAILED, "이미지 업로드에 실패했습니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다."),
    ALREADY_DISLIKED(HttpStatus.CONFLICT, "이미 싫어요를 눌렀습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 한 적 없는 글입니다."),
    DISLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "싫어요 한 적 없는 글입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
