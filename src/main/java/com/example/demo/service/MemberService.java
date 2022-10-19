package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.RefreshToken;
import com.example.demo.dto.member.DurationTimeDto;
import com.example.demo.dto.member.SignInDto;
import com.example.demo.dto.member.SignUpDto;
import com.example.demo.dto.member.TokenDto;
import com.example.demo.exception.*;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.exception.ExceptionType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpDto signUpDto) {
        validateInfo(signUpDto);
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        memberRepository.save(SignUpDto.toEntity(signUpDto));
    }

    private void validateInfo(SignUpDto signUpDto) {
        if(memberRepository.existsByUsername(signUpDto.getUsername()))
            throw new CustomException(USERNAME_ALREADY_EXISTS_EXCEPTION);
        if(memberRepository.existsByNickname(signUpDto.getNickname()))
            throw new CustomException(NICKNAME_ALREADY_EXISTS_EXCEPTION);
    }

    public TokenDto signIn(SignInDto signInDto) {
        Member member = memberRepository.findByUsername(signInDto.getUsername()).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND_EXCEPTION));
        if (!validatePassword(signInDto, member)) {
            throw new CustomException(LOGIN_FAILURE_EXCEPTION);
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        refreshTokenRepository.save(new RefreshToken(refreshToken, member.getId()));
        return new TokenDto(accessToken, refreshToken);
    }

    private boolean validatePassword(SignInDto signInDto, Member member) {
        return passwordEncoder.matches(signInDto.getPassword(), member.getPassword());
    }

    public void updateDT(DurationTimeDto durationTimeDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_EXCEPTION));
        member.updateDurationTime(durationTimeDto.getDurationTime());
        memberRepository.save(member);
    }
}
