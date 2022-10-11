package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.user.SignInRequest;
import com.example.demo.dto.user.SignUpRequest;
import com.example.demo.dto.user.TokenDto;
import com.example.demo.exception.*;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpRequest signUpRequest) {
        validateInfo(signUpRequest);
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        memberRepository.save(SignUpRequest.toEntity(signUpRequest));
    }

    private void validateInfo(SignUpRequest signUpRequest) {
        if(memberRepository.existsByUsername(signUpRequest.getUsername()))
            throw new EmailAlreadyExistsException(signUpRequest.getUsername());
        if(memberRepository.existsByNickname(signUpRequest.getNickname()))
            throw new NicknameAlreadyExistsException(signUpRequest.getNickname());
    }

    public TokenDto signIn(SignInRequest signInRequest) {
        Member member = memberRepository.findByUsername(signInRequest.getUsername()).orElseThrow(MemberNotFoundException::new);
        if (!validatePassword(signInRequest, member)) {
            throw new LoginFailureException();
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        member.updateRefreshToken(refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }

    private boolean validatePassword(SignInRequest signInRequest, Member member) {
        if(passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
            return true;
        else
            return false;
    }

}
