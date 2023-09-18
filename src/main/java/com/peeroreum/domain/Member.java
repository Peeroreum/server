package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;

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
    private Long grade;

    @Column
    private Long durationTime;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "member_wedu",
            joinColumns = { @JoinColumn(name = "member_id") },
            inverseJoinColumns = { @JoinColumn(name = "wedu_id") }
    )
    private Set<Wedu> wedus = new HashSet<>();

    @Builder
    public Member(String username, String password, String nickname, String image, Long grade) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.grade = grade;
        durationTime = 0L;
        this.roles = Collections.singletonList(Role.USER);
    }

    public void updateDurationTime(Long durationTime) {
        this.durationTime += durationTime;
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

}
