package com.example.demo.controller;

import com.example.demo.dto.user.SignInRequest;
import com.example.demo.dto.user.SignUpRequest;
import com.example.demo.dto.response.Response;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@RequestBody SignUpRequest signUpRequest) {
        memberService.signUp(signUpRequest);
        return Response.success();
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@RequestBody SignInRequest signInRequest) {
        return Response.success(memberService.signIn(signInRequest));
    }
}
