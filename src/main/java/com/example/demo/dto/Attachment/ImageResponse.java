package com.example.demo.dto.Attachment;

import com.example.demo.domain.Image;
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
