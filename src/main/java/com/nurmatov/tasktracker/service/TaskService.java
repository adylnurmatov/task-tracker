package com.nurmatov.tasktracker.service;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.dto.TaskRequest;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(Long taskStateId);

    TaskDto createTask(Long taskState, TaskRequest request);

    TaskDto updateTaskDto(Long taskStateId, TaskRequest request);

    void deleteTaskDto(Long id);
}
