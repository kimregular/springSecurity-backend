package org.example.springsecuritybackend.global.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie createCookie(String key, String value, Integer expiredS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        // https 사용 설정
        cookie.setMaxAge(expiredS);
        return cookie;
    }
}
