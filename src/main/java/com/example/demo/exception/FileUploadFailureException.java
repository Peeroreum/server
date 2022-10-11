package com.example.demo.exception;

import java.io.IOException;

public class FileUploadFailureException extends Throwable {
    public FileUploadFailureException(IOException e) {
    }
}
