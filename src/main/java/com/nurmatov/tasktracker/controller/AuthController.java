package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.dto.UserRequest;
import com.nurmatov.tasktracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRequest userRequest) {
        Optional<UserDto> user = userService.create(userRequest);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
