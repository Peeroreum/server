package com.example.demo.dto.Attachment;

import com.example.demo.domain.Image;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {
    private String imageName;
    private String imagePath;
    private Long imageSize;

    @Builder
    public ImageDto(Image image) {
        this.imageName = image.getImageName();
        this.imagePath = image.getImagePath();
        this.imageSize = image.getImageSize();
    }
}