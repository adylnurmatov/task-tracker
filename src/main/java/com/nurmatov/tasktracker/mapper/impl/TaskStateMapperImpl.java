package com.nurmatov.tasktracker.mapper.impl;

import com.nurmatov.tasktracker.dto.TaskStateDto;
import com.nurmatov.tasktracker.entity.TaskState;
import com.nurmatov.tasktracker.mapper.TaskMapper;
import com.nurmatov.tasktracker.mapper.TaskStateMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TaskStateMapperImpl implements TaskStateMapper {
    private final TaskMapper taskMapper;

    @Override
    public TaskStateDto toDto(TaskState taskState) {
        return TaskStateDto.builder()
                .id(taskState.getId())
                .name(taskState.getName())
                .createdAt(taskState.getCreatedAt())
                .leftTaskStateId(taskState.getLeftTaskState().map(TaskState::getId).orElse(null))
                .rightTaskStateId(taskState.getRightTaskState().map(TaskState::getId).orElse(null))

                .tasks(taskState.getTasks().stream().map(taskMapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
