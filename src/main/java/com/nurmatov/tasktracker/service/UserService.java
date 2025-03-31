package com.nurmatov.tasktracker.service;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.dto.UserRequest;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> findById(Long id);

    Optional<UserDto> create(UserRequest userRequest);

    Optional<UserDto> update(Long userId, UserDto userDto);

    Optional<UserDto> delete(Long id);
}
