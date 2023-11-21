package com.peeroreum.domain;

import javax.persistence.*;

@Entity
public class HashTag extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tag;
}
