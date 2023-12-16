package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.dto.member.SignInDto;
import com.peeroreum.dto.member.SignUpDto;
import com.peeroreum.dto.member.LogInDto;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.security.jwt.JwtTokenProvider;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
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

    public void signUp(SignUpDto signUpDto) {
        validateInfo(signUpDto);
        if(signUpDto.getPassword() != null) {
            signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        }
        memberRepository.save(SignUpDto.toEntity(signUpDto));
    }

    private void validateInfo(SignUpDto signUpDto) {
        validateEmail(signUpDto.getUsername());
        validateNickname(signUpDto.getNickname());
    }

    public boolean validateEmail(String email) {
        if(memberRepository.existsByUsername(email))
            throw new CustomException(ExceptionType.USERNAME_ALREADY_EXISTS_EXCEPTION);
        else {
            return true;
        }
    }

    public boolean validateNickname(String nickname) {
        if(memberRepository.existsByNickname(nickname))
            throw new CustomException(ExceptionType.NICKNAME_ALREADY_EXISTS_EXCEPTION);
        else {
            return true;
        }
    }
    public String socialSignIn(String email) {
        if(memberRepository.existsByUsername(email)) {
            String accessToken = jwtTokenProvider.createAccessToken(email);
            return accessToken;
        } else {
            throw new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION);
        }
    }

    public LogInDto signIn(SignInDto signInDto) {
        Member member = memberRepository.findByUsername(signInDto.getUsername()).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        if (!validatePassword(signInDto, member)) {
            throw new CustomException(ExceptionType.LOGIN_FAILURE_EXCEPTION);
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String nickname = member.getNickname();
        return new LogInDto(accessToken, refreshToken, nickname);
    }

    private boolean validatePassword(SignInDto signInDto, Member member) {
        return passwordEncoder.matches(signInDto.getPassword(), member.getPassword());
    }

}
