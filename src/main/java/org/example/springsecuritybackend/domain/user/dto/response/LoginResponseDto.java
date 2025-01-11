package org.example.springsecuritybackend.domain.user.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final String response;
    private final String message;

    public LoginResponseDto(String response, String message) {
        this.response = response;
        this.message = message;
    }
}
