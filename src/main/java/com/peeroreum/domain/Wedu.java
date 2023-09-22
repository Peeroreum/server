package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Image image;

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

    @OneToOne(mappedBy = "wedu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Invitation invitation;

    @ManyToMany(mappedBy = "wedus")
    private Set<Member> attendants = new HashSet<>();

    @Builder
    public Wedu(String title, Image image, int maximumPeople, boolean isSearchable, boolean isLocked, String password, Long grade, Long subject, Long gender) {
        this.title = title;
        this.image = image;
        this.maximumPeople = maximumPeople;
        this.isSearchable = isSearchable;
        this.isLocked = isLocked;
        this.password = password;
        this.grade = grade;
        this.subject = subject;
        this.gender = gender;
    }

    public void setMember(Member member) {
        if(!attendants.contains(member)) {
            this.attendants.add(member);
            member.setWedus(this);
        }
    }

}
