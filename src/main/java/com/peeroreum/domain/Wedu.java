package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String image;

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

    @Builder
    public Wedu(String title, String image, int maximumPeople, boolean isSearchable, boolean isLocked, String password, Long grade, Long subject, Long gender) {
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


}
