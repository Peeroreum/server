package com.peeroreum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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

    private String groupId;

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

    public void updateGroupId(Long parentId) {
        this.groupId = "groupId" + parentId;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.content = "";
        isDeleted = true;
    }

}