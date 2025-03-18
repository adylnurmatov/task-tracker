package com.nurmatov.tasktracker.service.impl;

import com.nurmatov.tasktracker.dto.ProjectDto;
import com.nurmatov.tasktracker.entity.Project;
import com.nurmatov.tasktracker.exception.BadRequestException;
import com.nurmatov.tasktracker.exception.NotFoundException;
import com.nurmatov.tasktracker.mapper.ProjectMapper;
import com.nurmatov.tasktracker.repository.ProjectRepository;
import com.nurmatov.tasktracker.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;


    @Override
    public ProjectDto createProject(String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("project name cannot be empty");
        }
        projectRepository
                .findByName(name)
                .ifPresent(project ->
                {
                    throw new BadRequestException("project with that name was created  already");
                });


        Project project = projectRepository.saveAndFlush(
                Project.builder()
                        .name(name)
                        .build()
        );
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto updateProject(Long id, String name) {
        if (name.trim().isEmpty()) {
            throw new BadRequestException("project name cannot be empty");
        }
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("cannot find project"));
        projectRepository
                .findByName(name)
                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), id))
                .ifPresent(anotherProject -> {
                    throw new BadRequestException("project already exist");
                });
        project.setName(name);
        project = projectRepository.saveAndFlush(project);
        return projectMapper.toDto(project);


    }

    @Override
    public List<ProjectDto> fetchProjects(Optional<String> optionalPrefixname) {
        optionalPrefixname = optionalPrefixname.filter(prefixName -> !prefixName.trim().isEmpty());
        Stream<Project> projectStream = optionalPrefixname
                .map(projectRepository::streamAllByNameStartingWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);
        return projectStream
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("cannot find project"));
        projectRepository.deleteById(id);
    }


}
