package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
public class Response {
    private String status;
    private int code;
    private Result result;

    public static Response success() {
        return new Response("success", 200, null);
    }

    public static <T> Response success(T data) {
        return new Response("success", 200, new Success<>(data));
    }

    public static Response fail(int code, String msg) {
        return new Response("fail", code, new Fail(msg));
    }
}
