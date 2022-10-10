package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.user.SignInRequest;
import com.example.demo.dto.user.SignUpRequest;
import com.example.demo.dto.user.TokenDto;
import com.example.demo.exception.*;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        memberRepository.save(SignUpRequest.toEntity(signUpRequest, passwordEncoder));
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
        String accessToken = jwtTokenProvider.createToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        member.updateRefreshToken(refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }

//    @Transactional
//    public TokenDto reIssue(TokenDto tokenDto) {
//        if(!jwtTokenProvider.validateToken(tokenDto.getRefreshToken()))
//            throw new InvalidRefreshTokenException();
//        Member member = findMemberByToken(tokenDto);
//
//        if(!member.getRefreshToken().equals(tokenDto.getRefreshToken()))
//            throw new InvalidRefreshTokenException();
//        String accessToken = jwtTokenProvider.createToken(member.getUsername());
//        String refreshToken = jwtTokenProvider.createRefreshToken();
//        member.updateRefreshToken(refreshToken);
//        return new TokenDto(accessToken, refreshToken);
//    }
//    public Member findMemberByToken(TokenDto tokenDto) {
//        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();
//        return memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
//    }

    private boolean validatePassword(SignInRequest signInRequest, Member member) {
        if(passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
            return true;
        else
            return false;
    }

}
