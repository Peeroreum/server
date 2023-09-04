package com.peeroreum.dto.Attachment;

import com.peeroreum.domain.Image;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String imageName;
    private String imagePath;
    private Long imageSize;

    @Builder
    public ImageDto(Image image) {
        this.id = image.getId();
        this.imageName = image.getImageName();
        this.imagePath = image.getImagePath();
        this.imageSize = image.getImageSize();
    }
}