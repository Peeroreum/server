package com.peeroreum.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Invitation extends EntityTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String backgroundColor;
    private String textColor;
    @OneToOne
    @JoinColumn(name = "wedu_id")
    private Wedu wedu;


}
