package com.example.demo.domain;

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

    @Column
    private String imgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Answer answer;

    @Builder
    public Image(String imgName) {
        this.imgName = imgName;
    }

    public void initQuestion(Question question) {
        if(this.question == null)
            this.question = question;
    }

    public void initAnswer(Answer answer) {
        if(this.answer == null)
            this.answer = answer;
    }
}