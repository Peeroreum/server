package com.example.demo.controller;

import com.example.demo.dto.Attachment.ImageDto;
import com.example.demo.dto.response.ResponseDto;
import com.example.demo.service.attachment.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @CrossOrigin
    @GetMapping(value = "/thumbnail/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseDto getThumbnail(@PathVariable Long id) throws IOException {
        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path;

        if(id != 0) {
            ImageDto image = imageService.findByImageId(id);
            path = image.getImagePath();
        }
        else {
            path = "images" + File.separator + "thumbnail" + File.separator + "thumbnail.png";
        }

        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return ResponseDto.success(imageByteArray);
    }

    @CrossOrigin
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseDto getImage(@PathVariable Long id) throws IOException {
        ImageDto image = imageService.findByImageId(id);
        String absolutePath
                = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = image.getImagePath();

        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return ResponseDto.success(imageByteArray);
    }
}
