package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Proof extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "proof", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> imageList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Wedu wedu;

    @Builder
    Proof(List<Image> imageList, Member member, Wedu wedu) {
        this.imageList = imageList;
        this.member = member;
        this.wedu = wedu;
    }

    public void addImage(Image image) {
        this.imageList.add(image);

        if(image.getProof() != this)
            image.setProof(this);
    }

}
