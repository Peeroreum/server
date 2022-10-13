package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "refresh_token_id")
    private String refreshToken;

    @Column
    private Long memberId;

}
