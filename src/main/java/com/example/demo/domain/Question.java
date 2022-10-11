package com.example.demo.domain;

import com.example.demo.dto.question.QuestionUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class Question extends EntityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    @Builder
    public Question(String content, String subject, Member member, List<Image> images) {
        this.content = content;
        this.subject = subject;
        this.member = member;
        this.images = new ArrayList<>();
        addImages(images);
    }

    public ImageUpdatedResult update(QuestionUpdateDto updateDto) {
        this.content = updateDto.getContent();
        this.subject = updateDto.getSubject();
        ImageUpdatedResult result = findupdatedImage(updateDto.getAddedImages(), updateDto.getDeletedImages());
        addImages(result.addedImages);
        deleteImages(result.deletedImages);
        return result;
    }

    private void addImages(List<Image> added) {
        added.stream().forEach(i -> {
            images.add(i);
            i.initQuestion(this);
        });
    }

    private void deleteImages(List<Image> deleted) {
        deleted.stream().forEach(i -> this.images.remove(i));
    }

    private ImageUpdatedResult findupdatedImage(List<MultipartFile> addedFiles, List<Long> deletedImageIds) {
        List<Image> addedImages = convertFilesToImages(addedFiles);
        List<Image> deletedImages = convertIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedFiles, addedImages, deletedImages);
    }

    private List<Image> convertFilesToImages(List<MultipartFile> files) {
        return files.stream().map(file -> new Image(file.getOriginalFilename())).collect(Collectors.toList());
    }

    private List<Image> convertIdsToImages(List<Long> imageIds) {
        return imageIds.stream().map(id -> convertIdToImage(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(Collectors.toList());
    }

    private Optional<Image> convertIdToImage(Long id) {
        return this.images.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult {
        private List<MultipartFile> addedFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }

}