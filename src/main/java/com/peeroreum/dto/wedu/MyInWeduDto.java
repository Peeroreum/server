package com.peeroreum.dto.wedu;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyInWeduDto {
    List<WeduDto> ingWedus;
    List<WeduDto> endWedus;
}
