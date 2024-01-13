package com.nurmatov.tasktracker.mapper;

import com.nurmatov.tasktracker.dto.TaskStateDto;
import com.nurmatov.tasktracker.entity.TaskState;

public interface TaskStateMapper {
    TaskStateDto toDto(TaskState taskState);

}
