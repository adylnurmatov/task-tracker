package com.nurmatov.tasktracker.service.impl;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.dto.UserRequest;
import com.nurmatov.tasktracker.entity.Authority;
import com.nurmatov.tasktracker.entity.Role;
import com.nurmatov.tasktracker.entity.User;
import com.nurmatov.tasktracker.exception.NotFoundException;
import com.nurmatov.tasktracker.mapper.UserMapper;
import com.nurmatov.tasktracker.repository.UserRepository;
import com.nurmatov.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserDto> findById(Long id) {
        UserDto userDto = userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
        return Optional.of(userDto);
    }

    @Override
    public Optional<UserDto> create(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        Authority authority = new Authority();
        authority.setRole(Role.ROLE_CLIENT.name());
        user.getAuthorities().add(authority);
        userRepository.save(user);
        return Optional.of(userMapper.toUserDto(user));
    }

    @Override
    public Optional<UserDto> update(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        return Optional.of(userDto);
    }

    @Override
    public Optional<UserDto> delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
        return Optional.of(userMapper.toUserDto(user));
    }
}
