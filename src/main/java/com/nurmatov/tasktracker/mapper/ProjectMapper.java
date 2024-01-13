package com.nurmatov.tasktracker.mapper;

import com.nurmatov.tasktracker.dto.ProjectDto;
import com.nurmatov.tasktracker.entity.Project;

public interface ProjectMapper {
    ProjectDto toDto(Project project);
}
