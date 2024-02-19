package com.peeroreum.domain;

import com.peeroreum.domain.image.Image;
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_image_id")
    private List<Image> images = new ArrayList<>();

    private Long groupId;

    private Long parentAnswerId;

    private boolean isSelected = false;

    private boolean isDeleted = false;

    @Builder
    public Answer(String content, Member member, Question question, Long parentAnswerId) {
        this.content = content;
        this.member = member;
        this.question = question;
        this.parentAnswerId = parentAnswerId;
    }

    public void updateGroupId() {
        if(this.parentAnswerId == -1) {
            this.groupId = this.id;
        } else {
            this.groupId = this.parentAnswerId;
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateImages(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void delete() {
        this.content = "";
        isDeleted = true;
    }

}