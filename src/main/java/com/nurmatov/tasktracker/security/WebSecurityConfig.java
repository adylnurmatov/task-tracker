package com.nurmatov.tasktracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurmatov.tasktracker.entity.Authority;
import com.nurmatov.tasktracker.entity.User;
import com.nurmatov.tasktracker.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration, UserService userService, JwtTokenService jwtTokenService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationConfiguration, userService, jwtTokenService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    session.disable();
                })
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                                .requestMatchers("/api/**").hasAnyRole("CLIENT", "ADMIN")
                                .anyRequest().authenticated())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomAuthorizationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth
                                .baseUri("/api/auth/oauth2/authorization")
                        )
                        .redirectionEndpoint(redir -> redir
                                .baseUri("/api/auth/oauth2/login/code/*")
                        )
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String email = oAuth2User.getAttribute("email");
                            System.out.println("Email: " + email);

                            User user = userService.findByEmail(email);
                            List<String> roles = user.getAuthorities().stream()
                                    .map(Authority::getRole)
                                    .toList();
                            String jwt = jwtTokenService.createAccessToken(user.getUsername(), roles, request.getRequestURL().toString());

                            response.setContentType(APPLICATION_JSON_VALUE);
                            Map<String, String> responseBody = new HashMap<>();
                            responseBody.put("token", jwt);
                            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                        })

                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(APPLICATION_JSON_VALUE);
                            Map<String, String> responseBody = new HashMap<>();
                            responseBody.put("error", "OAuth login failed");
                            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                        })
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/api/auth/logout")
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setStatus(HttpServletResponse.SC_OK);
                                    response.setContentType(APPLICATION_JSON_VALUE);
                                    Map<String, String> responseBody = new HashMap<>();
                                    responseBody.put("message", "Logged out successfully");
                                    new ObjectMapper().writeValue(response.getOutputStream(),
                                            responseBody);
                                })
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        users.setUsersByUsernameQuery(
                "SELECT username, password, enabled FROM users WHERE username = ?");

        users.setAuthoritiesByUsernameQuery(
                "SELECT u.username, a.role " +
                        "FROM users u " +
                        "JOIN authorities a ON u.id = a.user_id " +
                        "WHERE u.username = ?");

        return users;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService(UserService userService) {
        return request -> {
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);
            return userService.processOAuthPostLogin(oauth2User);
        };
    }

}
