package org.example.springsecuritybackend.global.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JWTConfig {

    @Value("${spring.secrets.API_KEY}")
    private String secret;

    /**
     * 이 메서드는 SecretKey를 생성하여 Bean으로 등록합니다.
     * Keys.hmacShaKeyFor 메서드를 사용하여 HMAC-SHA 알고리즘에 맞는 SecretKey를 생성합니다.
     * @return SecretKey HMAC-SHA 알고리즘을 사용하는 SecretKey 객체
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
