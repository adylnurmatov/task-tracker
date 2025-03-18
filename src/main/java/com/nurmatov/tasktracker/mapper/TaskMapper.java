package com.nurmatov.tasktracker.mapper;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.entity.Task;

import java.util.List;

public interface TaskMapper {
    TaskDto toDto(Task task);
    List<TaskDto> toDtos(List<Task> tasks);
    Task toEntity(TaskDto taskDto);
}
