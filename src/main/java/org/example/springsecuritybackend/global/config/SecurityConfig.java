package org.example.springsecuritybackend.global.config;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritybackend.global.jwt.JWTFilter;
import org.example.springsecuritybackend.global.jwt.JWTUtil;
import org.example.springsecuritybackend.global.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // 암호 해싱
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        // stateless 상태로 관리하기 때문에 굳이 켜놓을 필요 없다.

        http.formLogin(AbstractHttpConfigurer::disable);
        // jwt 를 사용할 예정이므로 form으로 로그인할 필요가 없다.

        http.httpBasic(AbstractHttpConfigurer::disable);
        // jwt 를 사용할 예정이므로 form으로 로그인할 필요가 없다.



        /*
        동작 순서는 위에서부터다 -> 서순을 주의할것
        requestMatchers("/path/**") : 해당하는 경로의 요청이 왔을경우
        anyRequest() : 그외 다른 모든 경로의 요청이 왔을 경우
         */

        /*
        permitAll() : 로그인을 하지 않아도 모든 사용자가 접근 가능
        authenticated() : 로그인을 완료한 사용자만 접근 가능
        hasRole() : 특정 role을 만족하는 사용자만 접근 가능
        denyAll() : 로그인을 완료 했더라도 모든 사용자가 접근 불가능
         */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/join", "/api/v1/login", "/api/v1").permitAll()
                .requestMatchers("/api/v1/admin").hasRole("ADMIN")
                .requestMatchers("/api/v1/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/v1/login");
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        // UsernamePasswordAuthenticationFilter는 원래 form 로그인에 쓰이는 필터이지만 JWT 검증용으로 개조시켜 사용함
        // 따라서 개조한 필터가 딱 원래 필터 위치에서 실행되도록 `addFilterAt()`메서드로 위치를 정해줌

        // 세션 설정
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));


        return http.build();
    }
}
