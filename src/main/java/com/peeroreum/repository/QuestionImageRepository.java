package com.peeroreum.repository;

import com.peeroreum.domain.Question;
import com.peeroreum.domain.image.QuestionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionImageRepository extends JpaRepository<QuestionImage, Long> {
    Optional<QuestionImage> findByQuestion(Question question);
    void deleteByQuestion(Question question);
}
