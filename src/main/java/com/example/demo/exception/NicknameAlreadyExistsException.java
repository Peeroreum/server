package com.example.demo.exception;

public class NicknameAlreadyExistsException extends RuntimeException {
    public NicknameAlreadyExistsException(String msg) {
        super(msg);
    }
}
