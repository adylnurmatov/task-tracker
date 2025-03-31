package com.nurmatov.tasktracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String role;

    private List<ProjectDto> projects;
}
