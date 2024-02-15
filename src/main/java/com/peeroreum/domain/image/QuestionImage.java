package com.peeroreum.domain.image;

import com.peeroreum.domain.EntityTime;
import com.peeroreum.domain.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class QuestionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_image_id")
    private List<Image> images;

    public QuestionImage(Question question, List<Image> images) {
        this.question = question;
        this.images = images;
    }
}
