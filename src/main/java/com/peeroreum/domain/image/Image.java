package com.peeroreum.domain.image;

import com.peeroreum.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Image extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageName;

    @Column
    private String imagePath;

    private Long imageSize;

    @Builder
    public Image(String imageName, String imagePath, Long imageSize) {

        this.imageName = imageName;
        this.imagePath = imagePath;
        this.imageSize = imageSize;
    }

}