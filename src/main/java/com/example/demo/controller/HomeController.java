package com.example.demo.controller;

import com.example.demo.domain.Member;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.example.demo.exception.ExceptionType.MEMBER_NOT_FOUND_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;

    @GetMapping("/home")
    public ResponseDto home(Principal principal) {
        String username = principal.getName();
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_EXCEPTION));
        String nickname = member.getNickname();
        return ResponseDto.success(nickname);
    }
}
