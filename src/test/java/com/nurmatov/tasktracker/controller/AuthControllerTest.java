package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.UserDto;
import com.nurmatov.tasktracker.dto.UserRequest;
import com.nurmatov.tasktracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_WhenValidRequest_ReturnsUserDto() {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("test@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");

        Mockito.when(userService.create(request)).thenReturn(Optional.of(userDto));

        ResponseEntity<UserDto> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void register_WhenUserServiceReturnsEmpty_ReturnsBadRequest() {
        UserRequest request = new UserRequest();
        request.setUsername("invalid");
        request.setPassword("123");
        request.setEmail("bad@example.com");

        Mockito.when(userService.create(request)).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}

