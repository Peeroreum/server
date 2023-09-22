package com.peeroreum.dto.wedu;

import com.peeroreum.domain.Image;
import com.peeroreum.domain.Wedu;

public class WeduDto {

    private String title;
    private Image image;
    private Long grade;
    private int attendingPeopleNum;
    private int dDay;

    public WeduDto(Wedu wedu, int dDay) {
        this.title = wedu.getTitle();
        this.image = wedu.getImage();
        this.grade = wedu.getGrade();
        this.attendingPeopleNum = wedu.getAttendants().size();
        this.dDay = dDay;
    }

    public WeduDto(Wedu wedu) {
        this.title = wedu.getTitle();
        this.image = wedu.getImage();
        this.grade = wedu.getGrade();
        this.attendingPeopleNum = wedu.getAttendants().size();
    }

}
