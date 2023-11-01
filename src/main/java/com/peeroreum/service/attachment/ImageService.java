package com.peeroreum.service.attachment;

import com.peeroreum.domain.Image;
import com.peeroreum.dto.Attachment.ImageDto;
import com.peeroreum.exception.CustomException;
import com.peeroreum.repository.ImageRepository;
import com.peeroreum.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public ImageDto findByImageId(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.IMAGE_NOT_FOUND_EXCEPTION));
        ImageDto imageDto = new ImageDto(image);
        return imageDto;
    }

    @Transactional
    public List<ImageDto> findAllByQuestion(Long questionId) {
        List<Image> imageList = imageRepository.findAllByQuestionId(questionId);
        return imageList.stream().map(ImageDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<ImageDto> findAllByAnswer(Long answerId) {
        List<Image> imageList = imageRepository.findAllByAnswerId(answerId);
        return imageList.stream().map(ImageDto::new).collect(Collectors.toList());
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(()->new CustomException(ExceptionType.IMAGE_NOT_FOUND_EXCEPTION));
        imageRepository.delete(image);
    }

}