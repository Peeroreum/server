package com.peeroreum.domain;

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
    private Long id;

    @Column
    private String refreshToken;

    @Column
    private Long memberId;

}
