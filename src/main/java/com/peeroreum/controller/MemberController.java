package com.peeroreum.controller;

import com.peeroreum.dto.member.ProfileImageDto;
import com.peeroreum.dto.member.SignInDto;
import com.peeroreum.dto.member.SignUpDto;
import com.peeroreum.dto.notification.FirebaseTokenDto;
import com.peeroreum.dto.response.ResponseDto;
import com.peeroreum.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
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

    @PostMapping("/member/firebasetoken")
    public ResponseDto postFirebaseToken(@RequestBody FirebaseTokenDto firebaseTokenDto, Principal principal) {
        return ResponseDto.success(memberService.setFirebaseToken(firebaseTokenDto, principal.getName()));
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

    @GetMapping("/member/friend/{nickname}")
    public ResponseDto getFriends(@PathVariable String nickname) {
        return ResponseDto.success(memberService.getFriendsList(nickname));
    }

    @GetMapping("/member/profile")
    public ResponseDto findFriends(@RequestParam String nickname, Principal principal) {
        return ResponseDto.success(memberService.findProfile(nickname, principal.getName()));
    }

    @PutMapping("/member/change/nickname")
    public ResponseDto putNickname(@RequestParam String nickname, Principal principal) {
        return ResponseDto.success(memberService.changeNickname(nickname, principal.getName()));
    }

    @PutMapping("/member/change/profileImage")
    public ResponseDto putProfileImage(@ModelAttribute ProfileImageDto profileImageDto, Principal principal) {
        return ResponseDto.success(memberService.changeProfileImage(profileImageDto, principal.getName()));
    }

    @PutMapping("/member/delete/profileImage")
    public ResponseDto deleteProfileImage(Principal principal) {
        return ResponseDto.success(memberService.deleteProfileImage(principal.getName()));
    }

}
