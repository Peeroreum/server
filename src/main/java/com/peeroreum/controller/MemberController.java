package com.peeroreum.controller;

import com.peeroreum.dto.member.ProfileImageDto;
import com.peeroreum.dto.member.SignInDto;
import com.peeroreum.dto.member.SignUpDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseDto.success(memberService.signUp(signUpDto));
    }

    @PostMapping("/login")
    public ResponseDto signIn(@RequestBody SignInDto signInDto) {
        return ResponseDto.success(memberService.signIn(signInDto));
    }

    @GetMapping("/socialLogin")
    public ResponseDto socialSignIn(@RequestParam String email) {
        return ResponseDto.success(memberService.socialSignIn(email));
    }

    @GetMapping("/signup/email/{email}")
    public ResponseDto checkEmail(@PathVariable String email) {
        return ResponseDto.success(memberService.validateEmail(email));
    }

    @GetMapping("/signup/nickname/{nickname}")
    public ResponseDto checkNickname(@PathVariable String nickname) {
        return ResponseDto.success(memberService.validateNickname(nickname));
    }

    @PutMapping("/member/change/pw")
    public ResponseDto putPassword(@RequestParam String password, Principal principal) {
        return ResponseDto.success(memberService.changePassword(password, principal.getName()));
    }

    @PostMapping("/member/friend/follow")
    public ResponseDto postFriends(@RequestParam String nickname, Principal principal) {
        return ResponseDto.success(memberService.followFriend(nickname, principal.getName()));
    }

    @DeleteMapping("/member/friend/unfollow")
    public ResponseDto deleteFriends(@RequestParam String nickname, Principal principal) {
        return ResponseDto.success(memberService.unFollowFriend(nickname, principal.getName()));
    }

    @GetMapping("/member/friend")
    public ResponseDto getFriends(Principal principal) {
        return ResponseDto.success(memberService.getFriendsList(principal.getName()));
    }

    @GetMapping("/member/profile")
    public ResponseDto findFriends(@RequestParam String nickname) {
        return ResponseDto.success(memberService.findProfile(nickname));
    }

    @PutMapping("/member/change/nickname")
    public ResponseDto putNickname(@RequestParam String nickname, Principal principal) {
        return ResponseDto.success(memberService.changeNickname(nickname, principal.getName()));
    }

    @PutMapping("/member/change/profileImage")
    public ResponseDto putProfileImage(@ModelAttribute ProfileImageDto profileImageDto, Principal principal) {
        return ResponseDto.success(memberService.changeProfileImage(profileImageDto, principal.getName()));

    }

}
