package com.peeroreum.domain;

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
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private Long likes = 0L;
    private boolean isDeleted;

    @Builder
    public Answer(String content, Member member, Question question) {
        this.content = content;
        this.member = member;
        this.question = question;
        this.isDeleted = false;
    }

    public void update(String content) {
        this.content = content;
    }

    public void updateLikes(int like) {
        this.likes += like;
    }

    public void addImage(Image image) {
        this.images.add(image);

        if(image.getAnswer() != this) {
            image.setAnswer(this);
        }
    }

    public void clearImage() {
        this.images.clear();
    }

    public void delete() {
        this.content = "";
        isDeleted = true;
    }
}