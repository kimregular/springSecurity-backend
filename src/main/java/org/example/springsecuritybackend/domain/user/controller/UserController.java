package org.example.springsecuritybackend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.dto.request.JoinRequestDto;
import org.example.springsecuritybackend.domain.user.dto.request.LoginRequestDto;
import org.example.springsecuritybackend.domain.user.dto.response.HomeResponseDto;
import org.example.springsecuritybackend.domain.user.dto.response.LoginResponseDto;
import org.example.springsecuritybackend.domain.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public HomeResponseDto home() {
        return new HomeResponseDto();
    }

    @PostMapping("/join")
    public String postJoin(JoinRequestDto joinRequestDto) {
        userService.joinProcess(joinRequestDto);
        return "ok";
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
