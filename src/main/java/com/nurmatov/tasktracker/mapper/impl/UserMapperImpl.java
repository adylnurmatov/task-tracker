package com.nurmatov.tasktracker.mapper.impl;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.entity.User;
import com.nurmatov.tasktracker.mapper.ProjectMapper;
import com.nurmatov.tasktracker.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final ProjectMapper projectMapper;
    @Override
    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setProjects(userDto.getProjects().stream().map(projectMapper::toEntity).collect(Collectors.toList()));
        return user;
    }

    @Override
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getAuthorities().toString());
        userDto.setProjects(user.getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList()));
        return userDto;
    }
}
