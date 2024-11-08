package org.example.springsecuritybackend.global.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final SecretKey secretKey; // jwtconfig 클래스에서 주입받는 비밀키

    /**
     * 유저이름을 검증하는 메서드
     * 내부의 비밀키를 이용하여 입력받은 토큰을 검증한다.
     *
     * @param token 문자열 형태의 토큰을 전달받는다.
     * @return String username
     */
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey) // 암호화가 되어있는 토큰을 서버의 비밀키로 검증 -> 우리 서버에서 발급한 토큰임?
                .build().parseSignedClaims(token) // JWT를 파싱
                .getPayload() // payload 부분에서
                .get("username", String.class); // 유저이름 가져오는데 해당 값이 String 이다!
    }

    /**
     * 유저 권한 검증 메서드
     *
     * @param token
     * @return
     */
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 토큰이 만료되었는지 검사
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    /**
     * 최초 로그인의 경우 토큰을 발급하는 메서드
     *
     * @param username 유저 이름
     * @param role     유저 권한
     * @param expireMs 토큰 유지 시간
     * @return
     */
    public String createJwt(String username, String role, Long expireMs) {
        return Jwts.builder() // 토큰을 만들자
                .claim("username", username) // 페이로드에 들어갈 클레임1
                .claim("role", role) // 페이로드에 들어갈 클레임2
                .issuedAt(new Date(System.currentTimeMillis())) // 페이로드에 들어갈 클레임3 -> 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expireMs)) // 페이로드에 들어갈 클레임4 -> 만료 시간
                .signWith(secretKey) // 비밀키로 암호화 진행
                .compact(); // JWT를 최종적으로 직렬화하여 문자열 형태로 반환
        // Base64로 인코딩된 문자열 형태의 최종 JWT를 생성
    }
}
