package com.example.demo.controller;

import com.example.demo.dto.member.SignInDto;
import com.example.demo.dto.member.SignUpDto;
import com.example.demo.dto.response.Response;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public Response signUp(@RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return Response.success();
    }

    @GetMapping("/login")
    public Response signIn(@RequestBody SignInDto signInDto) {
        return Response.success(memberService.signIn(signInDto));
    }
}
