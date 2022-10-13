package com.example.demo.dto.Attachment;

import com.example.demo.domain.Image;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ImageResponse {
    private Long imageId;

    public ImageResponse(Image image) {
        this.imageId = image.getId();
    }
}
