package com.nurmatov.tasktracker.service;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.dto.UserRequest;
import com.nurmatov.tasktracker.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> findById(Long id);

    Optional<UserDto> create(UserRequest userRequest);

    Optional<UserDto> update(Long userId, UserDto userDto);

    Optional<UserDto> delete(Long id);

    Optional<UserDto> findByUsername(String username);

    OAuth2User processOAuthPostLogin(OAuth2User oAuth2User);

    User findByEmail(String email);
}
