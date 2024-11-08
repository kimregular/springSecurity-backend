package org.example.springsecuritybackend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.dto.request.JoinRequestDto;
import org.example.springsecuritybackend.domain.user.entity.User;
import org.example.springsecuritybackend.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User joinProcess(JoinRequestDto joinRequestDto) {
        String username = joinRequestDto.username();
        String password = joinRequestDto.password();

        Boolean isExists = userRepository.existsByUsername(username);

        if (isExists) {
            throw new IllegalArgumentException(String.format("이미 존재하는 회원입니다. username = %s", username));
        }

        User user = User.of(username, bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }
}
