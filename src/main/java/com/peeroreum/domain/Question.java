package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private Long subject;

    @Column
    private Long detailSubject;

    @Column
    private Long grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_image_id")
    private List<Image> images;

    @Builder
    public Question(String title, String content, Long subject, Long detailSubject, Long grade, Member member) {
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.detailSubject = detailSubject;
        this.grade = grade;
        this.member = member;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateImages(List<Image> images) {
        this.images = images;
    }

}