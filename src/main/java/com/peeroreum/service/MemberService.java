package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.member.*;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.security.jwt.JwtTokenProvider;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.service.attachment.ImageService;
import com.peeroreum.service.attachment.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final S3Service s3Service;
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
    public LogInDto socialSignIn(String email) {
        Member member = memberRepository.findByUsername(email).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String nickname = member.getNickname();
        return new LogInDto(accessToken, refreshToken, email, nickname);
    }

    public LogInDto signIn(SignInDto signInDto) {
        Member member = memberRepository.findByUsername(signInDto.getUsername()).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        if (!validatePassword(signInDto, member)) {
            throw new CustomException(ExceptionType.LOGIN_FAILURE_EXCEPTION);
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String nickname = member.getNickname();
        String email = member.getUsername();
        return new LogInDto(accessToken, refreshToken, email, nickname);
    }

    private boolean validatePassword(SignInDto signInDto, Member member) {
        return passwordEncoder.matches(signInDto.getPassword(), member.getPassword());
    }

    public String changePassword(String password, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        member.updatePassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return "비밀번호 변경 완료";
    }

    public String followFriend(String nickname, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Member friend = memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(member.getNickname().equals(friend.getNickname())) {
            throw new CustomException(ExceptionType.SELF_FOLLOW_WRONG_EXCEPTION);
        }

        if(member.getFriends().contains(friend)) {
            throw new CustomException(ExceptionType.FRIEND_ALREADY_EXISTS_EXCEPTION);
        }

        member.addFriend(friend);
        friend.addFriend(member);

        memberRepository.save(member);
        memberRepository.save(friend);

        return "친구 팔로우 성공";
    }

    public String unFollowFriend(String nickname, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Member friend = memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(!member.getFriends().contains(friend)) {
            throw new CustomException(ExceptionType.FRIEND_NOT_FOUND_EXCEPTION);
        }

        member.removeFriend(friend);
        friend.removeFriend(member);

        memberRepository.save(member);
        memberRepository.save(friend);

        return "친구 언팔로우 성공";
    }

    public List<MemberProfileDto> getFriendsList(String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        List<Member> friends = member.getFriends();
        List<MemberProfileDto> friendProfiles = new ArrayList<>();

        for(Member friend : friends) {
            MemberProfileDto friendProfile = new MemberProfileDto(friend.getGrade(), friend.getImage() == null? null : friend.getImage().getImagePath(), friend.getNickname());
            friendProfiles.add(friendProfile);
        }

        return friendProfiles;
    }

    public MemberProfileDto findProfile(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        return new MemberProfileDto(member.getGrade(), member.getImage() == null? null : member.getImage().getImagePath(), member.getNickname(), member.getFriends().size());
    }

    public String changeNickname(String nickname, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        if(ChronoUnit.DAYS.between(LocalDateTime.now(), member.getModifiedTime()) < 30) {
            throw new CustomException(ExceptionType.CANNOT_CHANGE_NICKNAME);
        }
        if(validateNickname(nickname)) {
            member.updateNickname(nickname);
        }
        memberRepository.save(member);
        return "닉네임 변경 성공";
    }

    public Object changeProfileImage(ProfileImageDto profileImageDto, String name) {
        Member member = memberRepository.findByUsername(name).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(profileImageDto.getProfileImage() == null) {
            throw new CustomException(ExceptionType.FILETYPE_WRONG_EXCEPTION);
        }

        if(member.getImage() != null) {
            imageService.deleteImage(member.getImage().getId());
        }

        Image image = imageService.saveImage(profileImageDto.getProfileImage());
        member.updateImage(image);
        memberRepository.save(member);

        return "프로필 이미지 변경 성공";
    }
}
