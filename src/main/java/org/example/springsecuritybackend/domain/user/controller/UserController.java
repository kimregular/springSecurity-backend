package org.example.springsecuritybackend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.entity.Account;
import org.example.springsecuritybackend.domain.user.dto.request.AccountDto;
import org.example.springsecuritybackend.domain.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;
	private final UserService userService;

	@PostMapping("/signup")
	public String signup(AccountDto accountDto) {
		Account account = mapper.map(accountDto,
		                             Account.class);
		account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
		userService.createUser(account);
		return "redirect:/";
	}
=======
}
