package com.peeroreum.dto.wedu;

import com.peeroreum.domain.Wedu;

public class WeduSaveDto {

    private String title;
    private String image;
    private int maximumPeople;
    private boolean isSearchable;
    private boolean isLocked;
    private String password;
    private Long grade;
    private Long subject;
    private Long gender;

    public static Wedu toEntity(WeduSaveDto weduSaveDto) {
        return Wedu.builder()
                .title(weduSaveDto.title)
//                .image(weduSaveDto.image) 이미지 s3에 저장 과정 거친 후 DB 등록하도록 수정 필요
                .maximumPeople(weduSaveDto.maximumPeople)
                .isSearchable(weduSaveDto.isSearchable)
                .isLocked(weduSaveDto.isLocked)
                .password(weduSaveDto.password)
                .grade(weduSaveDto.grade)
                .subject(weduSaveDto.subject)
                .gender(weduSaveDto.gender)
                .build();
    }
}
