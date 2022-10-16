package com.example.demo.security;

import com.example.demo.domain.Member;
import com.example.demo.domain.Role;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.exception.ExceptionType.MEMBER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(()->new CustomException(MEMBER_NOT_FOUND_EXCEPTION));

        if(user != null) {
            return CustomUserDetails.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(Role.USER)
                    .nickname(user.getNickname())
                    .image(user.getImage())
                    .build();
        }
        return null;
    }
}
