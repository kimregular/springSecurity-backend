package org.example.springsecuritybackend.domain.user.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final String response = "OK";
    private final String message = "YOU ARE LOGGED IN!";
}
