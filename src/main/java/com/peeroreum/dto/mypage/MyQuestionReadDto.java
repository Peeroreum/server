package com.peeroreum.dto.mypage;

import com.peeroreum.dto.question.QuestionListReadDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyQuestionReadDto {
    private int total;
    private List<QuestionListReadDto> questionListReadDtos;
}
