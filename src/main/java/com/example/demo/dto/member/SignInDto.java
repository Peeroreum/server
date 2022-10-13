package com.example.demo.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInDto {
    private String username;
    private String password;
}
