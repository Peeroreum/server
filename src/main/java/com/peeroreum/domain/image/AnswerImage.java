package com.peeroreum.domain.image;

import com.peeroreum.domain.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class AnswerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_image_id")
    private List<Image> images;

    public AnswerImage(Answer answer, List<Image> images) {
        this.answer = answer;
        this.images = images;
    }
}
