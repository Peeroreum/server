package com.peeroreum.dto.wedu;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyProgress {
    List<Long> monthlyProgress;

    public MonthlyProgress(List<Long> monthlyProgress) {
        this.monthlyProgress = monthlyProgress;
    }
}
