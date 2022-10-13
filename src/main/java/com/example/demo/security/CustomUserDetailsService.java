package com.example.demo.security;

import com.example.demo.domain.Member;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(MemberNotFoundException::new);

        if(user != null) {
            return CustomUserDetails.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRoles().get(0))
                    .nickname(user.getNickname())
                    .image(user.getImage())
                    .build();
        }
        return null;
    }
}
