package org.example.springsecuritybackend.global.jwt;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.entity.User;
import org.example.springsecuritybackend.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("해당하는 유저가 없습니다. name = $s", username)));
        return new CustomUserDetails(user);
    }
}
