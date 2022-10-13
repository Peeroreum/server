package com.example.demo.service.attachment;

import com.example.demo.domain.Image;
import com.example.demo.exception.ImageNotFoundException;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public Image findByImageId(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);

        return image;
    }

    @Transactional
    public List<Image> findAllByQuestion(Long questionId) {
        List<Image> imageList = imageRepository.findAllByQuestionId(questionId);

        return imageList;
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
        imageRepository.delete(image);
    }
}
