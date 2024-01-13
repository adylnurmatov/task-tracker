package com.nurmatov.tasktracker.service;

import com.nurmatov.tasktracker.dto.TaskStateDto;

import java.util.List;

public interface TaskStateService {
    List<TaskStateDto> getTaskStates(Long projectId);

    TaskStateDto createTaskState(Long projectId, String name);

    TaskStateDto updateTaskStateDto(Long taskStateId, String name);

    void deleteTaskStateDto(Long id);
}
