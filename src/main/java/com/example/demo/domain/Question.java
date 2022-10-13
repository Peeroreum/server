package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Question(String content, String subject, Member member) {
        this.content = content;
        this.subject = subject;
        this.member = member;
    }

    public void update(String content, String subject) {
        this.content = content;
        this.subject = subject;
    }
    public void addImage(Image image) {
        this.images.add(image);

        if(image.getQuestion() != this)
            image.setQuestion(this);
    }
}