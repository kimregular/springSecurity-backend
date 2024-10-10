package org.example.springsecuritybackend.domain.user.controller;

import org.example.springsecuritybackend.domain.user.response.HomeResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping
    public HomeResponseDto home() {
        return new HomeResponseDto();
    }
}
