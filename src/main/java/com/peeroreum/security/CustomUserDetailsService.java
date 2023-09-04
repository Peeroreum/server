package com.peeroreum.security;

import com.peeroreum.domain.Member;
import com.peeroreum.domain.Role;
import com.peeroreum.exception.CustomException;
import com.peeroreum.repository.MemberRepository;
import com.peeroreum.exception.ExceptionType;
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
                .orElseThrow(()->new CustomException(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION));

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
