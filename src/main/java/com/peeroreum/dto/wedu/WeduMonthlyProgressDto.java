package com.peeroreum.dto.wedu;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WeduMonthlyProgressDto {
    List<Long> monthlyProgress;
    LocalDate createdDate;
    LocalDate targetDate;

    public WeduMonthlyProgressDto(List<Long> monthlyProgress, LocalDate createdDate, LocalDate targetDate) {
        this.monthlyProgress = monthlyProgress;
        this.createdDate = createdDate;
        this.targetDate = targetDate;
    }
}
