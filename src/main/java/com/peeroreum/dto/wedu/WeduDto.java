package com.peeroreum.dto.wedu;

import com.peeroreum.domain.Wedu;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
public class WeduDto {

    private Long id;
    private String title;
    private String imagePath;
    private Long grade;
    private Long subject;
    private int attendingPeopleNum;
    private Long dDay;
    private Long progress;
    private boolean isLocked;
    private String password;

    public WeduDto(Wedu wedu, int attendingPeopleNum) {
        this.id = wedu.getId();
        this.title = wedu.getTitle();
        this.imagePath = wedu.getImage().getImagePath();
        this.grade = wedu.getGrade();
        this.subject = wedu.getSubject();
        this.isLocked = wedu.isLocked();
        this.password = wedu.getPassword();
        this.attendingPeopleNum = attendingPeopleNum;
        this.dDay = LocalDate.now().until(wedu.getTargetDate(), ChronoUnit.DAYS);
    }

    public WeduDto(Wedu wedu, int attendingPeopleNum, Long progress) {
        this.id = wedu.getId();
        this.title = wedu.getTitle();
        this.imagePath = wedu.getImage().getImagePath();
        this.grade = wedu.getGrade();
        this.subject = wedu.getSubject();
        this.attendingPeopleNum = attendingPeopleNum;
        this.dDay = LocalDate.now().until(wedu.getTargetDate(), ChronoUnit.DAYS);
        this.progress = progress;
    }

}
