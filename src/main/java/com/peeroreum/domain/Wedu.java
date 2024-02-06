package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private String challenge;

    @Column
    private boolean isLocked;

    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member host;

    @Setter
    @OneToMany(mappedBy = "wedu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HashTag> hashTags = new HashSet<>();
    @Builder
    public Wedu(String title, Image image, Member host, int maximumPeople, boolean isLocked, String password, Long grade, Long subject, LocalDate targetDate, String challenge) {
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

    public void updateImage(Image image) {
        this.image = image;
    }

    public void updateMaximum(int maximumPeople) {
        this.maximumPeople = maximumPeople;
    }

    public void updateLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateHashTags(Set<HashTag> hashTags) {
        this.getHashTags().clear();
        this.getHashTags().addAll(hashTags);
    }
}
