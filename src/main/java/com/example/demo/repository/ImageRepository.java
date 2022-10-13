package com.example.demo.repository;

import com.example.demo.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Override
    Optional<Image> findById(Long aLong);

    List<Image> findAllByQuestionId(Long questionId);

}
