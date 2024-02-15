package com.peeroreum.repository;

import com.peeroreum.domain.Answer;
import com.peeroreum.domain.image.AnswerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerImageRepository extends JpaRepository<AnswerImage, Long> {
    Optional<AnswerImage> findByAnswer(Answer answer);
    void deleteByAnswer(Answer answer);
}
