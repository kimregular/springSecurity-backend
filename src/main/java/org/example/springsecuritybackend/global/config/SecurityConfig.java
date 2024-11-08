package org.example.springsecuritybackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // 암호 해싱
        return new BCryptPasswordEncoder();
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
                .requestMatchers("/api/v1/join","/api/v1/login").permitAll()
                .requestMatchers("/api/v1/admin").hasRole("ADMIN")
                .requestMatchers("/api/v1/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
        );

        // 세션 설정
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));


        return http.build();
    }
}
