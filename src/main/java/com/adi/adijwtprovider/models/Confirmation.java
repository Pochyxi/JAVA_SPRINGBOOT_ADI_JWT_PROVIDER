package com.adi.adijwtprovider.models;

import com.adi.adijwtprovider.enums.TokenType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Confirmation {

    private Long id;

    private String token;

    private TokenType tokenType;

    private LocalDateTime createdDate;

    private User user;

    public Confirmation( User user) {
        this.user = user;
        this.createdDate = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }
}
