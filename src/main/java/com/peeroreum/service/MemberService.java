package com.peeroreum.service;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.image.Image;
import com.peeroreum.dto.member.*;
import com.peeroreum.dto.notification.FCMNotificationRequestDto;
import com.peeroreum.dto.notification.FirebaseTokenDto;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.security.jwt.JwtTokenProvider;
import com.peeroreum.exception.CustomException;
import com.peeroreum.exception.ExceptionType;
import com.peeroreum.service.attachment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FCMNotificationService notificationService;

    public LogInDto signUp(SignUpDto signUpDto) {
        validateInfo(signUpDto);
        if(signUpDto.getPassword() != null) {
            signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        }
        memberRepository.save(SignUpDto.toEntity(signUpDto));

        return socialSignIn(signUpDto.getUsername());
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
        Long grade = member.getGrade();
        String profileImage = member.getImage() == null? null : member.getImage().getImagePath();
        return new LogInDto(accessToken, refreshToken, email, nickname, grade, profileImage);
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
        Long grade = member.getGrade();
        String profileImage = member.getImage() == null? null : member.getImage().getImagePath();
        return new LogInDto(accessToken, refreshToken, email, nickname, grade, profileImage);
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

    public String followFriend(String nickname, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Member friend = memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(member.getNickname().equals(friend.getNickname())) {
            throw new CustomException(ExceptionType.SELF_FOLLOW_WRONG_EXCEPTION);
        }

        if(member.getFriends().contains(friend)) {
            throw new CustomException(ExceptionType.FRIEND_ALREADY_EXISTS_EXCEPTION);
        }
        member.addFriend(friend);
        memberRepository.save(member);

        friendNotification(member, friend);
        return "친구 팔로우 성공";
    }

    private void friendNotification(Member member, Member friend) {
        FCMNotificationRequestDto notificationRequestDto = new FCMNotificationRequestDto(friend.getNickname(), "마이페이지", member.getNickname() + " 님이 회원님을 친구로 추가했어요.");
        notificationService.sendNotificationByToken(notificationRequestDto);
    }

    public String unFollowFriend(String nickname, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Member friend = memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(!member.getFriends().contains(friend)) {
            throw new CustomException(ExceptionType.FRIEND_NOT_FOUND_EXCEPTION);
        }

        member.removeFriend(friend);
        memberRepository.save(member);

        return "친구 언팔로우 성공";
    }

    public List<MemberProfileDto> getFriendsList(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        List<Member> friends = member.getFriends();
        List<MemberProfileDto> friendProfiles = new ArrayList<>();

        for(Member friend : friends) {
            MemberProfileDto friendProfile = new MemberProfileDto(friend.getGrade(), friend.getImage() == null? null : friend.getImage().getImagePath(), friend.getNickname());
            friendProfiles.add(friendProfile);
        }

        return friendProfiles;
    }

    public MemberProfileDto findProfile(String nickname, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Member findMember = memberRepository.findByNickname(nickname).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        findMember.checkActiveDaysCount();
        return new MemberProfileDto(findMember.getGrade(), findMember.getImage() == null? null : findMember.getImage().getImagePath(), findMember.getBackgroundImage() == null? null : findMember.getBackgroundImage().getImagePath(), findMember.getNickname(), findMember.getFriends().size(), member.getFriends().contains(findMember), findMember.getActiveDaysCount());
    }

    public String changeNickname(String nickname, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
//        if(ChronoUnit.DAYS.between(LocalDateTime.now(), member.getModifiedTime()) < 30) {
//            throw new CustomException(ExceptionType.CANNOT_CHANGE_NICKNAME);
//        }
        if(validateNickname(nickname)) {
            member.updateNickname(nickname);
        }
        memberRepository.save(member);
        return member.getNickname();
    }

    public String changeProfileImage(ProfileImageDto profileImageDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(profileImageDto.getProfileImage() == null) {
            throw new CustomException(ExceptionType.FILETYPE_WRONG_EXCEPTION);
        }

        if(member.getImage() != null) {
            imageService.deleteImage(member.getImage().getId());
        }

        Image image = imageService.saveImage(profileImageDto.getProfileImage());
        member.updateImage(image);
        memberRepository.save(member);

        return member.getImage().getImagePath();
    }

    public MemberProfileDto deleteProfileImage(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Image profileImage = member.getImage();
        if(profileImage != null) {
            imageService.deleteImage(profileImage.getId());
        }

        member.updateImage(null);
        memberRepository.save(member);
        return new MemberProfileDto(member.getGrade(), null, member.getNickname());
    }

    public Member setFirebaseToken(FirebaseTokenDto firebaseTokenDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        member.updateFirebaseToken(firebaseTokenDto.getFirebaseToken());
        memberRepository.save(member);

        return member;
    }

    public Object changeBackgroundImage(ProfileImageDto profileImageDto, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

        if(profileImageDto.getProfileImage() == null) {
            throw new CustomException(ExceptionType.FILETYPE_WRONG_EXCEPTION);
        }

        if(member.getBackgroundImage() != null) {
            imageService.deleteImage(member.getBackgroundImage().getId());
        }

        Image image = imageService.saveImage(profileImageDto.getProfileImage());
        member.updateBackgroundImage(image);
        memberRepository.save(member);

        return member.getBackgroundImage().getImagePath();
    }

    public MemberProfileDto deleteBackgroundImage(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        Image backgroundImage = member.getBackgroundImage();
        if(backgroundImage != null) {
            imageService.deleteImage(backgroundImage.getId());
        }

        member.updateBackgroundImage(null);
        member.checkActiveDaysCount();
        memberRepository.save(member);
        return new MemberProfileDto(member.getGrade(), member.getImage() == null? null : member.getImage().getImagePath(), null, member.getNickname(), member.getActiveDaysCount());
    }

    public Long updateActiveDays(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));
        member.checkActiveDaysCount();
        member.updateActiveDaysCount();
        memberRepository.save(member);
        return member.getActiveDaysCount();
    }
}
