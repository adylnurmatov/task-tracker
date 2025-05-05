package com.nurmatov.tasktracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    public CustomAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration,
                                      UserService userService,
                                      JwtTokenService jwtTokenService) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();

        Set<String> allowedRoles = Set.of("ROLE_CLIENT", "ROLE_ADMIN");

        boolean hasAllowedRole = user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> allowedRoles.contains(grantedAuthority.getAuthority()));

        if (!hasAllowedRole) {
            throw new AuthenticationException("Access denied: Only CLIENTS or ADMINS are allowed to login") {
            };
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();
        Optional<UserDto> userResponse = userService.findByUsername(user.getUsername());

        if (userResponse.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error", "User not found"));
            return;
        }

        Long userId = userResponse.get().getId();

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtTokenService.createAccessToken(user.getUsername(), roles, request.getRequestURL().toString());
        String refreshToken = jwtTokenService.createRefreshToken(user.getUsername(), roles, request.getRequestURI());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", userId.toString());
        responseBody.put("accessToken", accessToken);
        responseBody.put("refreshToken", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),
                Map.of("error_message", "Authentication failed: " + failed.getMessage()));
    }
}
