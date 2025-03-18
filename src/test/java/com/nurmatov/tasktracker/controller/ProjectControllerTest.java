package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.ProjectDto;
import com.nurmatov.tasktracker.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_ShouldReturnProjectDto() {
        String projectName = "Test Project";
        ProjectDto expectedDto = new ProjectDto();
        when(projectService.createProject(projectName)).thenReturn(expectedDto);

        ProjectDto result = projectController.createProject(projectName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(projectService, times(1)).createProject(projectName);
    }

    @Test
    void updateProject_ShouldReturnUpdatedProjectDto() {
        Long projectId = 1L;
        String newName = "Updated Project";
        ProjectDto expectedDto = new ProjectDto();
        when(projectService.updateProject(projectId, newName)).thenReturn(expectedDto);

        ProjectDto result = projectController.updateProject(projectId, newName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(projectService, times(1)).updateProject(projectId, newName);
    }

    @Test
    void fetchProjects_WithPrefix_ShouldReturnFilteredList() {
        Optional<String> prefix = Optional.of("Test");
        List<ProjectDto> expectedList = Arrays.asList(new ProjectDto(), new ProjectDto());
        when(projectService.fetchProjects(prefix)).thenReturn(expectedList);

        List<ProjectDto> result = projectController.fetchProjects(prefix);

        assertNotNull(result);
        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(projectService, times(1)).fetchProjects(prefix);
    }

    @Test
    void fetchProjects_WithoutPrefix_ShouldReturnAllProjects() {
        Optional<String> emptyPrefix = Optional.empty();
        List<ProjectDto> expectedList = Arrays.asList(new ProjectDto(), new ProjectDto());
        when(projectService.fetchProjects(emptyPrefix)).thenReturn(expectedList);

        List<ProjectDto> result = projectController.fetchProjects(emptyPrefix);

        assertNotNull(result);
        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(projectService, times(1)).fetchProjects(emptyPrefix);
    }

    @Test
    void deleteProject_ShouldCallServiceMethod() {
        Long projectId = 1L;
        doNothing().when(projectService).deleteProject(projectId);

        projectController.deleteProject(projectId);

        verify(projectService, times(1)).deleteProject(projectId);
    }

    @Test
    void createProject_WithNullName_ShouldHandleGracefully() {
        String nullName = null;
        ProjectDto expectedDto = new ProjectDto();
        when(projectService.createProject(nullName)).thenReturn(expectedDto);

        ProjectDto result = projectController.createProject(nullName);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(projectService, times(1)).createProject(nullName);
    }
}
