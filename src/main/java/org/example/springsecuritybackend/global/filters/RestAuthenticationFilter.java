package org.example.springsecuritybackend.global.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsecuritybackend.domain.user.dto.request.AccountDto;
import org.example.springsecuritybackend.global.token.RestAuthenticationToken;
import org.example.springsecuritybackend.global.util.WebUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public RestAuthenticationFilter(HttpSecurity http) {
		super(new AntPathRequestMatcher("/api/login",
		                                "POST"));
		setSecurityContextRepository(getSecurityContextRepository(http));
	}

	private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
		// 인증상태 세션 저장
		SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
		if (securityContextRepository == null) {
			securityContextRepository = new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
			                                                                    new HttpSessionSecurityContextRepository());
		}
		return securityContextRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
	                                                                                                      AuthenticationException,
	                                                                                                      IOException,
	                                                                                                      ServletException {
		if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
			throw new IllegalArgumentException("Authentication method is not Supported");
		}

		AccountDto accountDto = objectMapper.readValue(request.getReader(),
		                                               AccountDto.class);
		if (!StringUtils.hasText(accountDto.getUsername()) || !StringUtils.hasText(accountDto.getPassword())) {
			throw new AuthenticationServiceException("Username or Password is not provided");
		}

		RestAuthenticationToken authenticationToken = new RestAuthenticationToken(accountDto.getUsername(),
		                                                                          accountDto.getPassword());

		return this.getAuthenticationManager().authenticate(authenticationToken);
	}
}
