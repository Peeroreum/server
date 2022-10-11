package com.example.demo.dto.Attachment;

import com.example.demo.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String imgName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getImgName());
    }
}