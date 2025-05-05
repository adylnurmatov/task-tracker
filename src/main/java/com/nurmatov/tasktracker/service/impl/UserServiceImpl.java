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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        user.setPassword("{bcrypt}" + bCryptPasswordEncoder.encode(userRequest.getPassword()));
        System.out.println(userRequest.getPassword());
        Authority authority = new Authority();
        authority.setUser(user);
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
    @Transactional
    @Override
    public Optional<UserDto> findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return Optional.of(userMapper.toUserDto(user));
    }
}
