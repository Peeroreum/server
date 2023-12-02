package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wedu_id")
    private Wedu wedu;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    @Builder
    Invitation(Wedu wedu, Image image) {
        this.wedu = wedu;
        this.image = image;
    }

}
