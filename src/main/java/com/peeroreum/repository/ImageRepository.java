package com.peeroreum.repository;

import com.peeroreum.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByQuestionId(Long questionId);
    List<Image> findAllByAnswerId(Long answerId);

}
