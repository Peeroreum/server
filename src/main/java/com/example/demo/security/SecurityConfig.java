package com.example.demo.security;

import com.example.demo.security.jwt.JwtAuthenticationFilter;
import com.example.demo.security.jwt.JwtExceptionFilter;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CusmtomEntryPoint customEntryPoint;
    private final ObjectMapper objectMapper;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll();
//                .and()
//                .exceptionHandling().authenticationEntryPoint(customEntryPoint)
//                .accessDeniedHandler(customAccessDeniedHandler)
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);

    }

}
