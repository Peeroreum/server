package com.peeroreum.dto.wedu;

import com.peeroreum.domain.Wedu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WeduDto {

    private String title;
    private String imagePath;
    private Long grade;
    private Long subject;
    private int attendingPeopleNum;
    private Long dDay;

    public WeduDto(Wedu wedu, int attendingPeopleNum) {
        this.title = wedu.getTitle();
        this.imagePath = wedu.getImage().getImagePath();
        this.grade = wedu.getGrade();
        this.subject = wedu.getSubject();
        this.attendingPeopleNum = attendingPeopleNum;
        this.dDay = LocalDate.now().until(wedu.getTargetDate(), ChronoUnit.DAYS);
    }

}
