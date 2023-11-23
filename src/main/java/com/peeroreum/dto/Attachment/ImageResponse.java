package com.peeroreum.dto.Attachment;

import com.peeroreum.domain.image.Image;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ImageResponse {
    private String imagePath;

    public ImageResponse(Image image) {
        this.imagePath = image.getImagePath();
    }
}
