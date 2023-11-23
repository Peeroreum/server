package com.peeroreum.dto.wedu;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ChallengeSaveDto {
    List<MultipartFile> files;
}
