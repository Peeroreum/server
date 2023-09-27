package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Wedu extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private int maximumPeople;

    @Column
    private boolean isSearchable;

    @Column
    private boolean isLocked;

    private String password;

    @Column
    private Long grade;

    @Column
    private Long subject;

    @Column
    private Long gender;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member host;

    @OneToOne(mappedBy = "wedu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Invitation invitation;

    @ManyToMany(mappedBy = "wedus")
    private Set<Member> attendants = new HashSet<>();

    @Builder
    public Wedu(String title, Image image, Member host, int maximumPeople, boolean isSearchable, boolean isLocked, String password, Long grade, Long subject, Long gender) {
        this.title = title;
        this.image = image;
        this.host = host;
        this.maximumPeople = maximumPeople;
        this.isSearchable = isSearchable;
        this.isLocked = isLocked;
        this.password = password;
        this.grade = grade;
        this.subject = subject;
        this.gender = gender;
    }

    public void addAttendant(Member member) {
        if(!attendants.contains(member)) {
            this.attendants.add(member);
            member.setWedus(this);
        }
    }

}
