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
public class Answer extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Answer parent;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private Long likes = 0L;
    private Long dislikes = 0L;

    @Builder
    public Answer(String content, Member member, Question question, Answer parent) {
        this.content = content;
        this.member = member;
        this.question = question;
        this.parent = parent;
    }

    public void update(String content) {
    }

    public void updateLikes(int like) {
        this.likes += like;
    }
    public void updateDislikes(int dislike) {
         this.dislikes += dislike;
    }

    public void addImage(Image image) {
        this.images.add(image);

        if(image.getAnswer() != this) {
            image.setAnswer(this);
        }
    }

    public void delete() {
        this.content = "";
    }
}