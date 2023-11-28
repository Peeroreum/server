package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Wedu extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Long subject;

    @Column
    private LocalDate targetDate;

    @Column
    private Long grade;

    @Column
    private int maximumPeople;

    @Column
    private Long challenge;

    @Column
    private boolean isLocked;

    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member host;

    @Builder
    public Wedu(String title, Image image, Member host, int maximumPeople, boolean isLocked, String password, Long grade, Long subject, LocalDate targetDate, Long challenge) {
        this.title = title;
        this.image = image;
        this.host = host;
        this.maximumPeople = maximumPeople;
        this.isLocked = isLocked;
        this.password = password;
        this.grade = grade;
        this.subject = subject;
        this.targetDate = targetDate;
        this.challenge = challenge;
    }

    public void update(Image image, int maximumPeople, boolean isLocked, String password) {
        this.image = image;
        this.maximumPeople = maximumPeople;
        this.isLocked = isLocked;
        this.password = password;
    }

}
