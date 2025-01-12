package org.example.springsecuritybackend.global.provider;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.domain.user.dto.response.AccountContext;
import org.example.springsecuritybackend.global.token.RestAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("restAuthenticationProvider")
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String loginId = authentication.getName();
		String password = (String) authentication.getCredentials();

		AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(loginId);
		if (!passwordEncoder.matches(password,
		                             accountContext.getPassword())) {
			throw new BadCredentialsException("Invalid Password");
		}

		return new RestAuthenticationToken(accountContext.getAuthorities(),
		                                   accountContext.getAccountDto(),
		                                   null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(RestAuthenticationToken.class);
	}
}
