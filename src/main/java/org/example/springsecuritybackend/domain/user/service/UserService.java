package org.example.springsecuritybackend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.entity.Account;
import org.example.springsecuritybackend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public void createUser(Account account) {
		userRepository.save(account);
	}
}
