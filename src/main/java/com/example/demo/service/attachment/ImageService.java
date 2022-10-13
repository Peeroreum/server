package com.example.demo.service.attachment;

import com.example.demo.domain.Image;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.demo.exception.ExceptionType.IMAGE_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public Image findByImageId(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(()->new CustomException(IMAGE_NOT_FOUND_EXCEPTION));

        return image;
    }

    @Transactional
    public List<Image> findAllByQuestion(Long questionId) {
        List<Image> imageList = imageRepository.findAllByQuestionId(questionId);

        return imageList;
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(()->new CustomException(IMAGE_NOT_FOUND_EXCEPTION));
        imageRepository.delete(image);
    }
}
