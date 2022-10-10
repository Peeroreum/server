package com.example.demo.security;

import com.example.demo.domain.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class AuthHelper {

    public static boolean isAuthenticated() {
        return getAuthentication() instanceof CustomAuthenticationToken && getAuthentication().isAuthenticated();
    }
    public static Long extractUserId() {
        return Long.valueOf(getUserDetails().getUserId());
    }

    public static Set<Role> extractUserRoles() {
        return getUserDetails().getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .map(strAuth -> Role.valueOf(strAuth))
                .collect(Collectors.toSet());
    }

    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
