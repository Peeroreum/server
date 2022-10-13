package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    private String status;
    private int code;
    private T data;


    public static ResponseDto success() {
        return new ResponseDto("success", 0, null);
    }

    public static <T> ResponseDto success(T data) {
        return new ResponseDto("success", 0, data);
    }

    public static ResponseDto fail(int code, String msg) {
        return new ResponseDto("fail", code, msg);
    }
}
