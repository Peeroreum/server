package com.peeroreum.controller;

import com.peeroreum.domain.Member;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.peeroreum.exception.ExceptionType.MEMBER_NOT_FOUND_EXCEPTION;

@CrossOrigin
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
