package org.example.springsecuritybackend.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritybackend.global.util.CookieUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    // UsernamePasswordAuthenticationFilter -> form 로그인에 쓰이는 필터
    // 해당 필터를 개조해서 JWT 검증 필터로 사용한다.

    private final AuthenticationManager authenticationManager;
    // 요청 로직이 실행 되기 전 데이터를 검사하는 역할

    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        log.info("username, {}", username);
        // 요청이 들어오면 필터에서 유저이름과 비밀번호를 낚아챈다.

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        // 위에서 낚은 정보를 가지고 토큰을 생성

        return authenticationManager.authenticate(authToken);
        // 메니저에게 해당 정보의 토큰이 유효한지 확인받는다.
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // 위에서 진행한 검증이 성공했을 경우 호출되는 메서드
        log.warn("Login Success");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 1000 * 60 * 60 * 10L);

        response.addCookie(CookieUtil.createCookie("Authorization", token, 60*60*60));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 위에서 진행한 검증이 실패했을 경우 호출되는 메서드
        logger.warn("Login Fail!");

        response.setStatus(401);
    }
}
