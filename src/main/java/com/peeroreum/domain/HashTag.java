package com.peeroreum.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class HashTag extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tag;

    @ManyToOne
    private Wedu wedu;

    public HashTag(String tag, Wedu wedu) {

        this.tag = tag;
        this.wedu = wedu;
    }
}
