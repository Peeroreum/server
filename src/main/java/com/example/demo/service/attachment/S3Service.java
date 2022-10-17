package com.example.demo.service.attachment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.domain.Image;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket")
    private String bucket;
    private String baseurl = "https://befriends.s3.ap-northeast-2.amazonaws.com/";

    private final AmazonS3 amazonS3;

    public List<Image> uploadImage(List<MultipartFile> multipartFiles) {
        List<Image> images = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = createFilename(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new CustomException(ExceptionType.UPLOAD_FAILURE_EXCEPTION);
            }

            Image image = Image.builder()
                    .imageName(fileName)
                    .imagePath(baseurl + fileName)
                    .imageSize(file.getSize())
                    .build();
            images.add(image);
        });

        return images;
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFilename(String originalFilename) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    private String getFileExtension(String originalFilename) {
        try {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CustomException(ExceptionType.FILETYPE_WRONG_EXCEPTION);
        }
    }


}
