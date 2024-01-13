package com.nurmatov.tasktracker.service;


import com.nurmatov.tasktracker.dto.ProjectDto;
import com.nurmatov.tasktracker.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    ProjectDto createProject(String name);

    ProjectDto updateProject(Long id, String name);

    List<ProjectDto> fetchProjects(Optional<String> optionalPrefixname);

    void deleteProject(Long id);
}
