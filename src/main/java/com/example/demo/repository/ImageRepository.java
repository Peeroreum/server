package com.example.demo.repository;

import com.example.demo.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByQuestionId(Long questionId);
    List<Image> findAllByAnswerId(Long answerId);

}
