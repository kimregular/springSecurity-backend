package org.example.springsecuritybackend.global.service;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.dto.request.AccountDto;
import org.example.springsecuritybackend.domain.user.dto.response.AccountContext;
import org.example.springsecuritybackend.domain.user.entity.Account;
import org.example.springsecuritybackend.domain.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final ModelMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No User has Found with username : " + username));

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles()));
		AccountDto accountDto = mapper.map(account,
		                            AccountDto.class);
		return new AccountContext(accountDto,
		                          authorities);
	}
}
