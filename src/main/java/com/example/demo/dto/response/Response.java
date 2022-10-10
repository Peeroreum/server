package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
public class Response {
    private boolean status;
    private int code;
    private Result result;

    public static Response success() {
        return new Response(true, 0, null);
    }

    public static <T> Response success(T data) {
        return new Response(true, 0, new Success<>(data));
    }

    public static Response fail(int code, String msg) {
        return new Response(false, code, new Fail(msg));
    }
}
