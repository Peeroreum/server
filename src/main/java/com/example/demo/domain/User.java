package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String image;

    @Column
    private String grade;

    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String password, String nickname, String image, String grade, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.grade = grade;
        this.role = role;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
