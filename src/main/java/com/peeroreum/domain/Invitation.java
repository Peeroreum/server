package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
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

    @Builder
    Invitation(String content, String backgroundColor, String textColor, Wedu wedu) {
        this.content = content;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.wedu = wedu;
    }

    public void update(String content, String backgroundColor, String textColor) {
        this.content = content;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

}
