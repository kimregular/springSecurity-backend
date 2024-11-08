package org.example.springsecuritybackend.domain.user.controller;

import org.example.springsecuritybackend.domain.user.dto.request.LoginRequestDto;
import org.example.springsecuritybackend.domain.user.dto.response.HomeResponseDto;
import org.example.springsecuritybackend.domain.user.dto.response.LoginResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping
    public HomeResponseDto home() {
        return new HomeResponseDto();
    }

    @PostMapping("/login")
    public LoginResponseDto postLogin(LoginRequestDto loginRequestDto) {
        System.out.println(loginRequestDto.username());
        System.out.println(loginRequestDto.password());
        return new LoginResponseDto();
    }

    @GetMapping("/admin")
    public String adminP() {
        return "ADMIN CONTROLLER";
    }
}
