package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proof_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Proof proof;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Wedu wedu;

    @Builder
    public Image(String imageName, String imagePath, Long imageSize) {

        this.imageName = imageName;
        this.imagePath = imagePath;
        this.imageSize = imageSize;
    }

    public void setQuestion(Question question) {
        this.question = question;

        if(!question.getImages().contains(this))
            question.getImages().add(this);
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;

        if(!answer.getImages().contains(this))
            answer.getImages().add(this);
    }

    public void setProof(Proof proof) {
        this.proof = proof;

        if(!proof.getImageList().contains(this))
            answer.getImages().add(this);
    }

}