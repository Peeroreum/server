package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Fail implements Result {
    private String msg;
}
