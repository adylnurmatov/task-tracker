package com.nurmatov.tasktracker.mapper.impl;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.entity.Task;
import com.nurmatov.tasktracker.mapper.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .createdAt(task.getCreatedAt())
                .description(task.getDesctiption())

                .build();
    }
}
