package com.peeroreum.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ProfileImageDto {
    MultipartFile profileImage;
}
