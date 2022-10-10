package com.example.demo.dto;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private String image;
    private String grade;

    public static User toEntity(SignUpRequest signUpRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(signUpRequest.email)
                .password(passwordEncoder.encode(signUpRequest.password))
                .nickname(signUpRequest.nickname)
                .image(signUpRequest.image)
                .grade(signUpRequest.grade)
                .role(Role.USER)
                .build();
    }
}
