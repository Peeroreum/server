package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
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
    private Long id;

    @Column(nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String nickname;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column
    private Long grade;

    @Column
    private Long goodSubject;
    @Column
    private Long goodDetailSubject;
    @Column
    private Long goodLevel;

    @Column
    private Long badSubject;
    @Column
    private Long badDetailSubject;
    @Column
    private Long badLevel;

    @Column
    private String school;

    @ManyToMany
    private List<Member> friends = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Member(String username, String password, String nickname, Image image, Long grade, Long goodSubject, Long goodDetailSubject, Long goodLevel, Long badSubject, Long badDetailSubject, Long badLevel, String school) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.grade = grade;
        this.goodSubject = goodSubject;
        this.goodDetailSubject = goodDetailSubject;
        this.goodLevel = goodLevel;
        this.badSubject = badSubject;
        this.badDetailSubject = badDetailSubject;
        this.badLevel = badLevel;
        this.school = school;
        this.roles = Collections.singletonList(Role.USER);
    }

    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImage(Image image){
        this.image = image;
    }

    public void addFriend(Member member) {
        this.friends.add(member);
    }

    public void removeFriend(Member member) {
        this.friends.remove(member);
    }

}
