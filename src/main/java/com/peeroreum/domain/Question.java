package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private Long subject;

    @Column
    private Long grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private Long likes = 0L;

    @Builder
    public Question(String content, Long subject, Long grade, Member member) {
        this.content = content;
        this.subject = subject;
        this.grade = grade;
        this.member = member;
    }

    public void update(String content, Long subject, Long grade) {
        this.content = content;
        this.subject = subject;
        this.grade = grade;
    }

    public void updateLikes(int like) {
        this.likes += like;
    }

    public void addImage(Image image) {
        this.images.add(image);

        if(image.getQuestion() != this)
            image.setQuestion(this);
    }

    public void clearImage() {
        this.images.clear();
    }
}