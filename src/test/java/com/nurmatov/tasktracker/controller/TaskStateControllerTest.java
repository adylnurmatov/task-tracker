package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.TaskStateDto;
import com.nurmatov.tasktracker.service.TaskStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskStateControllerTest {

    @Mock
    private TaskStateService taskStateService;

    @InjectMocks
    private TaskStateController taskStateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskStates_ShouldReturnTaskStateList() {
        Long projectId = 1L;
        List<TaskStateDto> expectedList = Arrays.asList(new TaskStateDto(), new TaskStateDto());
        when(taskStateService.getTaskStates(projectId)).thenReturn(expectedList);

        List<TaskStateDto> result = taskStateController.getTaskStates(projectId);

        assertNotNull(result);
        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(taskStateService, times(1)).getTaskStates(projectId);
    }

    @Test
    void createTaskState_ShouldReturnCreatedTaskStateDto() {
        Long projectId = 1L;
        String taskStateName = "New Task State";
        TaskStateDto expectedDto = new TaskStateDto();
        when(taskStateService.createTaskState(projectId, taskStateName)).thenReturn(expectedDto);

        TaskStateDto result = taskStateController.createTaskState(projectId, taskStateName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskStateService, times(1)).createTaskState(projectId, taskStateName);
    }

    @Test
    void updateTaskStateDto_ShouldReturnUpdatedTaskStateDto() {
        Long taskStateId = 1L;
        String newName = "Updated Task State";
        TaskStateDto expectedDto = new TaskStateDto();
        when(taskStateService.updateTaskStateDto(taskStateId, newName)).thenReturn(expectedDto);

        TaskStateDto result = taskStateController.updateTaskStateDto(taskStateId, newName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskStateService, times(1)).updateTaskStateDto(taskStateId, newName);
    }

    @Test
    void deleteTaskStateDto_ShouldCallServiceMethod() {
        Long taskStateId = 1L;
        doNothing().when(taskStateService).deleteTaskStateDto(taskStateId);

        taskStateController.deleteTaskStateDto(taskStateId);

        verify(taskStateService, times(1)).deleteTaskStateDto(taskStateId);
    }

    @Test
    void createTaskState_WithNullName_ShouldHandleGracefully() {
        Long projectId = 1L;
        String nullName = null;
        TaskStateDto expectedDto = new TaskStateDto();
        when(taskStateService.createTaskState(projectId, nullName)).thenReturn(expectedDto);

        TaskStateDto result = taskStateController.createTaskState(projectId, nullName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskStateService, times(1)).createTaskState(projectId, nullName);
    }

    @Test
    void updateTaskStateDto_WithNullName_ShouldHandleGracefully() {
        Long taskStateId = 1L;
        String nullName = null;
        TaskStateDto expectedDto = new TaskStateDto();
        when(taskStateService.updateTaskStateDto(taskStateId, nullName)).thenReturn(expectedDto);

        TaskStateDto result = taskStateController.updateTaskStateDto(taskStateId, nullName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskStateService, times(1)).updateTaskStateDto(taskStateId, nullName);
    }

    @Test
    void getTaskStates_WithInvalidProjectId_ShouldHandleGracefully() {
        Long invalidProjectId = -1L;
        List<TaskStateDto> expectedList = Arrays.asList();
        when(taskStateService.getTaskStates(invalidProjectId)).thenReturn(expectedList);

        List<TaskStateDto> result = taskStateController.getTaskStates(invalidProjectId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskStateService, times(1)).getTaskStates(invalidProjectId);
    }
}
