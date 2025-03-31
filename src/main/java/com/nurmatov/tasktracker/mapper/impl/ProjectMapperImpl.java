package com.nurmatov.tasktracker.mapper.impl;

import com.nurmatov.tasktracker.dto.ProjectDto;
import com.nurmatov.tasktracker.entity.Project;
import com.nurmatov.tasktracker.mapper.ProjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapperImpl implements ProjectMapper {
    @Override
    public ProjectDto toDto(Project project) {
        return ProjectDto.builder()

                .id(project.getId())
                .name(project.getName())
                .createdAt(project.getCreatedAt())
                .build();
    }

    @Override
    public Project toEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setId(projectDto.getId());
        project.setName(projectDto.getName());
        project.setCreatedAt(projectDto.getCreatedAt());
        return project;
    }
}
