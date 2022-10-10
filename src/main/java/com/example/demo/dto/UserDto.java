package com.example.demo.dto;

import com.example.demo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    private String grade;
    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getNickname(), user.getGrade());
    }
}
