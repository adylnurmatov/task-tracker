package com.nurmatov.tasktracker.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigProperties {
    @Value("${jwt.secret}")
    public String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    public long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    public long refreshTokenExpiration;




    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
