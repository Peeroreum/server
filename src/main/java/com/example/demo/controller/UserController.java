package com.example.demo.controller;

import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.response.Response;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        userService.signUp(signUpRequest);
        return Response.success();
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@RequestBody SignInRequest signInRequest) throws Exception {
        return Response.success(userService.signIn(signInRequest));
    }
}
