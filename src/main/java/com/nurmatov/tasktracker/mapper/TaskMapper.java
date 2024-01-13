package com.nurmatov.tasktracker.mapper;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.entity.Task;

public interface TaskMapper {
    TaskDto toDto(Task task);
}
