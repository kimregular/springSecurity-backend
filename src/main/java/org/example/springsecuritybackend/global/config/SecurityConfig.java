package org.example.springsecuritybackend.global.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.global.handlers.FormAccessDeniedHandler;
import org.example.springsecuritybackend.global.handlers.FormAuthenticationFailureHandler;
import org.example.springsecuritybackend.global.handlers.FormAuthenticationSuccessHandler;
import org.example.springsecuritybackend.global.provider.FormAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	private final FormAuthenticationSuccessHandler successHandler;
	private final FormAuthenticationFailureHandler failureHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
	                                               FormAuthenticationProvider authenticationProvider) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/css/**",
						                 "/images/**",
						                 "/js/**",
						                 "/favicon.*",
						                 "/*/icon-*")
						.permitAll()
						.requestMatchers("/",
						                 "/login*",
						                 "/signup")
						.permitAll()
						.requestMatchers("/user")
						.hasAuthority("ROLE_USER")
						.requestMatchers("manager")
						.hasAuthority("ROLE_MANAGER")
						.requestMatchers("/admin")
						.hasAuthority("ROLE_ADMIN")
						.anyRequest()
						.authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.permitAll()
						.authenticationDetailsSource(authenticationDetailsSource)
						.successHandler(successHandler)
						// 비로그인 상태로 유저창에 들어가면 로그인필요 이후 로그인하면 '/' 이동되지 않고 '/user/'로 이동
						.failureHandler(failureHandler))
				// 로그인에 실패하면 에러 메시지가 변경됨
				//				.userDetailsService(userDetailsService); authenticationProvider를 구현했다면 사용하지 않음
				.authenticationProvider(authenticationProvider)
				.exceptionHandling(exception -> exception.accessDeniedHandler(new FormAccessDeniedHandler("/denied")));
		return http.build();
	}

	// userDetailsService를 구현했다면 더이상 사용하지 않음
	//	@Bean
	//	public UserDetailsService userDetailsService() {
	//		UserDetails user = User
	//				.withUsername("user")
	//				.password("{noop}1111")
	//				.roles("USER")
	//				.build();
	//		return new InMemoryUserDetailsManager(user);
	//	}
}
