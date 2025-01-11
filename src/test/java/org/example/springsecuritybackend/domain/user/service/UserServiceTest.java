package org.example.springsecuritybackend.domain.user.service;

import org.example.springsecuritybackend.domain.user.dto.request.JoinRequestDto;
import org.example.springsecuritybackend.domain.user.entity.User;
import org.example.springsecuritybackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 회원가입 테스트")
    void test1(){
        // given
        String username = "tester";
        String password = "password";
        JoinRequestDto joinRequestDto = new JoinRequestDto(username, password);
        // when
        User user = userService.joinProcess(joinRequestDto);
        // then
        assertThat(user.getPassword()).isNotEqualTo(password);
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("똑같은 이름의 유저는 회원가입 못한다.")
    void test2(){
        // given
        String username = "tester";
        String password = "password";
        JoinRequestDto joinRequestDto = new JoinRequestDto(username, password);
        // when
        User user = userService.joinProcess(joinRequestDto);
        // then
        assertThatThrownBy(() -> userService.joinProcess(joinRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("이미 존재하는 회원입니다. username = %s", username));
    }
}