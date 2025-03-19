package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.dto.TaskRequest;
import com.nurmatov.tasktracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTask_ShouldReturnTaskList() {
        Long taskStateId = 1L;
        List<TaskDto> expectedList = Arrays.asList(new TaskDto(), new TaskDto());
        when(taskService.getTasks(taskStateId)).thenReturn(expectedList);

        List<TaskDto> result = taskController.getTask(taskStateId);

        assertNotNull(result);
        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(taskService, times(1)).getTasks(taskStateId);
    }

    @Test
    void createTask_ShouldReturnCreatedTaskDto() {
        Long taskStateId = 1L;
        TaskRequest taskRequest = new TaskRequest();
        TaskDto expectedDto = new TaskDto();
        when(taskService.createTask(taskStateId, taskRequest)).thenReturn(expectedDto);

        TaskDto result = taskController.createTask(taskStateId, taskRequest);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskService, times(1)).createTask(taskStateId, taskRequest);
    }

    @Test
    void updateTaskDto_ShouldReturnUpdatedTaskDto() {
        Long taskId = 1L;
        TaskRequest taskRequest = new TaskRequest();
        TaskDto expectedDto = new TaskDto();
        when(taskService.updateTaskDto(taskId, taskRequest)).thenReturn(expectedDto);

        TaskDto result = taskController.updateTaskDto(taskId, taskRequest);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskService, times(1)).updateTaskDto(taskId, taskRequest);
    }

    @Test
    void deleteTaskDto_ShouldCallServiceMethod() {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTaskDto(taskId);

        taskController.deleteTaskDto(taskId);

        verify(taskService, times(1)).deleteTaskDto(taskId);
    }

    @Test
    void createTask_WithNullRequest_ShouldHandleGracefully() {
        Long taskStateId = 1L;
        TaskRequest nullRequest = null;
        TaskDto expectedDto = new TaskDto();
        when(taskService.createTask(taskStateId, nullRequest)).thenReturn(expectedDto);

        TaskDto result = taskController.createTask(taskStateId, nullRequest);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskService, times(1)).createTask(taskStateId, nullRequest);
    }

    @Test
    void updateTaskDto_WithNullRequest_ShouldHandleGracefully() {
        Long taskId = 1L;
        TaskRequest nullRequest = null;
        TaskDto expectedDto = new TaskDto();
        when(taskService.updateTaskDto(taskId, nullRequest)).thenReturn(expectedDto);

        TaskDto result = taskController.updateTaskDto(taskId, nullRequest);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskService, times(1)).updateTaskDto(taskId, nullRequest);
    }

    @Test
    void getTask_WithInvalidTaskStateId_ShouldHandleGracefully() {
        Long invalidTaskStateId = -1L;
        List<TaskDto> expectedList = Arrays.asList();
        when(taskService.getTasks(invalidTaskStateId)).thenReturn(expectedList);

        List<TaskDto> result = taskController.getTask(invalidTaskStateId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskService, times(1)).getTasks(invalidTaskStateId);
    }
}
