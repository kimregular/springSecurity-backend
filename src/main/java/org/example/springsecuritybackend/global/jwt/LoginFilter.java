package org.example.springsecuritybackend.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    // UsernamePasswordAuthenticationFilter form 로그인에 쓰이는 필터
    // 해당 필터를 개조해서 JWT 검증 필터로 사용한다.

    private final AuthenticationManager authenticationManager;
    // 요청 로직이 실행 되기 전 데이터를 검사하는 역할

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        // 요청이 들어오면 필터에서 유저이름과 비밀번호를 낚아챈다.

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        // 위에서 낚은 정보를 가지고 토큰을 생성

        return authenticationManager.authenticate(authToken);
        // 메니저에게 해당 정보의 토큰이 유효한지 확인받는다.
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 위에서 진행한 검증이 성공했을 경우 호출되는 메서드
        System.out.println("Login Success");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 위에서 진행한 검증이 실패했을 경우 호출되는 메서드
        System.out.println("Login Fail!");
    }
}
