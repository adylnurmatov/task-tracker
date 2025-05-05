package com.nurmatov.tasktracker.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;

    public CustomAuthorizationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String refreshTokenHeader = request.getHeader("Refresh-Token");

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String accessToken = authorizationHeader.substring("Bearer ".length());

                DecodedJWT decodedJWT = jwtTokenService.verifyToken(accessToken);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
                return;

            } else if (refreshTokenHeader != null) {
                String refreshToken = refreshTokenHeader;

                DecodedJWT decodedJWT = jwtTokenService.verifyToken(refreshToken);
                String username = decodedJWT.getSubject();
                List<String> roles = Arrays.asList(decodedJWT.getClaim("roles").asArray(String.class));

                String newAccessToken = jwtTokenService.createAccessToken(username, roles, request.getRequestURL().toString());

                response.setHeader("Access-Token", newAccessToken);
                filterChain.doFilter(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);

            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Authorization error: " + e.getMessage());
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

}

