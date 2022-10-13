package com.example.demo.controller;

import com.example.demo.dto.member.SignInDto;
import com.example.demo.dto.member.SignUpDto;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto signUp(@RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseDto.success();
    }

    @GetMapping("/login")
    public ResponseDto signIn(@RequestBody SignInDto signInDto) {
        return ResponseDto.success(memberService.signIn(signInDto));
    }
}
