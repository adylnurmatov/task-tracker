package com.nurmatov.tasktracker.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtTokenService {
    private final SecurityConfigProperties securityConfig;

    public JwtTokenService(SecurityConfigProperties securityConfigProperties) {
        this.securityConfig = securityConfigProperties;
    }

    public String createAccessToken(String username, List<String> roles, String issuer) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + securityConfig.accessTokenExpiration))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(securityConfig.jwtSecret.getBytes()));
    }

    public String createRefreshToken(String username, List<String> roles, String issuer) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + securityConfig.refreshTokenExpiration))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(securityConfig.jwtSecret.getBytes()));
    }

    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(securityConfig.jwtSecret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
