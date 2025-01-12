package org.example.springsecuritybackend.global.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.global.entrypoint.RestAuthenticationEntryPoint;
import org.example.springsecuritybackend.global.filters.RestAuthenticationFilter;
import org.example.springsecuritybackend.global.handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider formAuthenticationProvider;
    private final AuthenticationProvider restAuthenticationProvider;
    private final FormAuthenticationSuccessHandler formSuccessHandler;
    private final FormAuthenticationFailureHandler formFailureHandler;
    private final RestAuthenticationSuccessHandler restSuccessHandler;
    private final RestAuthenticationFailureHandler restFailureHandler;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(this::configureAuthorization)
                .formLogin(this::configureFormLogin)
                .authenticationProvider(formAuthenticationProvider)
                .exceptionHandling(exception -> exception.accessDeniedHandler(new FormAccessDeniedHandler("/denied")));
        return http.build();
    }

    private void configureFormLogin(FormLoginConfigurer<HttpSecurity> form) {
        form
                .loginPage("/login")
                .permitAll()
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(formSuccessHandler) // 비로그인 상태로 유저창에 들어가면 로그인필요 이후 로그인하면 '/' 이동되지 않고 '/user'로 이동
                .failureHandler(formFailureHandler);
    }

    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        configurePublicAccess(auth);
        configureRoleBasedAccess(auth);
        configureAuthenticatedAccess(auth);
    }

    private void configurePublicAccess(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers("/h2-console/**")
                .permitAll() // 인증인가없이도 해당 경로 접근 가능 설정
                .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*")
                .permitAll()
                .requestMatchers("/", "/login*", "/signup")
                .permitAll();
    }

    private void configureRoleBasedAccess(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers("/user")
                .hasAuthority("ROLE_USER")
                .requestMatchers("manager")
                .hasAuthority("ROLE_MANAGER")
                .requestMatchers("/admin")
                .hasAuthority("ROLE_ADMIN");
    }

    private void configureAuthenticatedAccess(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.anyRequest().authenticated();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**") // 해당 주소로 접근해야 아래의 설정이 적용됨
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*")
                        .permitAll()
                        .requestMatchers("/api", "/api/login")
                        .permitAll()
                        .requestMatchers("/api/user")
                        .hasAuthority("ROLE_USER")
                        .requestMatchers("/api/manager")
                        .hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/api/admin")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(restAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler()));
        return http.build();
    }

    private RestAuthenticationFilter restAuthenticationFilter(HttpSecurity http, AuthenticationManager authenticationManager) {
        RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter(http);
        restAuthenticationFilter.setAuthenticationManager(authenticationManager);
        restAuthenticationFilter.setAuthenticationSuccessHandler(restSuccessHandler);
        restAuthenticationFilter.setAuthenticationFailureHandler(restFailureHandler);
        return restAuthenticationFilter;
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
