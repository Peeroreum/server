package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.SignInResponse;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void signUp(SignUpRequest signUpRequest) throws Exception {
        validateInfo(signUpRequest);
        userRepository.save(SignUpRequest.toEntity(signUpRequest, passwordEncoder));
    }

    private void validateInfo(SignUpRequest signUpRequest) throws Exception {
        if(userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new Exception("이미 존재하는 아이디입니다.");
        if(userRepository.existsByNickname(signUpRequest.getNickname()))
            throw new Exception("이미 존재하는 닉네임입니다.");
    }

    public SignInResponse signIn(SignInRequest signInRequest) throws Exception {
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new Exception("회원이 존재하지 않습니다"));
        if (validatePassword(signInRequest, user)) {
            String subject = createSubject(user);
            String accessToken = tokenService.createAccessToken(subject);
            String refreshToken = tokenService.createRefreshToken(subject);
            return new SignInResponse(accessToken, refreshToken);
        }
        else throw new Exception("비밀번호가 일치하지 않습니다.");
    }

    private boolean validatePassword(SignInRequest signInRequest, User user) {
        if(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword()))
            return true;
        else
            return false;
    }

    private String createSubject(User user) {
        return String.valueOf(user.getId());
    }
}
