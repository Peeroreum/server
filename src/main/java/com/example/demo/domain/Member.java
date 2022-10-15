package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String image;

    @Column
    private String grade;

    @Column
    private String tier;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Member(String username, String password, String nickname, String image, String grade, String tier) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.grade = grade;
        this.tier = tier;
        this.roles = Collections.singletonList(Role.USER);
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImage(String image){
        this.image = image;
    }

    public void updateTier(String tier) {
        this.tier = tier;
    }
}
