package com.nurmatov.tasktracker.mapper;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.entity.User;

public interface UserMapper {

    User toUser(UserDto userDto);
    UserDto toUserDto(User user);

}
