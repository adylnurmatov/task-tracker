package com.nurmatov.tasktracker.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
}
