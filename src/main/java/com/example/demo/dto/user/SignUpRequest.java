package com.example.demo.dto.user;

import com.example.demo.domain.Role;
import com.example.demo.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private String nickname;
    private String image;
    private String grade;

    public static Member toEntity(SignUpRequest signUpRequest) {
        return Member.builder()
                .username(signUpRequest.username)
                .password(signUpRequest.password)
                .nickname(signUpRequest.nickname)
                .image(signUpRequest.image)
                .grade(signUpRequest.grade)
                .build();
    }
}
