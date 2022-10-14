package com.example.demo.service.attachment;

import com.example.demo.domain.Answer;
import com.example.demo.domain.Image;
import com.example.demo.domain.Question;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileHandler {

    public List<Image> parseFileInfo(Question question, List<MultipartFile> multipartFiles) throws IOException {
        List<Image> fileList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            String currentTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            String path = "images" + File.separator + currentTime;
            File file = new File(path);

            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            for(MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                if(ObjectUtils.isEmpty(contentType))
                    break;
                else {
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else break;
                }

                String new_file_name = System.nanoTime() + originalFileExtension;

                Image image = Image.builder()
                        .imageName(multipartFile.getOriginalFilename())
                        .imagePath(path + File.separator + new_file_name)
                        .imageSize(multipartFile.getSize())
                        .build();

                if(question.getId() != null)
                    image.setQuestion(question);

                fileList.add(image);

                file = new File(absolutePath + path + File.separator + new_file_name);
                multipartFile.transferTo(file);

                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return fileList;
    }

    public List<Image> parseFileInfo(Answer answer, List<MultipartFile> multipartFiles) throws IOException {
        List<Image> fileList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            String currentTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            String path = "images" + File.separator + currentTime;
            File file = new File(path);

            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            for(MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                if(ObjectUtils.isEmpty(contentType))
                    break;
                else {
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else break;
                }

                String new_file_name = System.nanoTime() + originalFileExtension;

                Image image = Image.builder()
                        .imageName(multipartFile.getOriginalFilename())
                        .imagePath(path + File.separator + new_file_name)
                        .imageSize(multipartFile.getSize())
                        .build();

                if(answer.getId() != null)
                    image.setAnswer(answer);

                fileList.add(image);

                file = new File(absolutePath + path + File.separator + new_file_name);
                multipartFile.transferTo(file);

                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return fileList;
    }
}
