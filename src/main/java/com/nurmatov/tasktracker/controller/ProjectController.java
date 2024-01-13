package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.ProjectDto;
import com.nurmatov.tasktracker.entity.Project;
import com.nurmatov.tasktracker.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Transactional
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;



    @PostMapping("/api/projects/create")
    public ProjectDto createProject(@RequestParam String name){
        return projectService.createProject(name);

    }

    @PatchMapping("/api/projects/{id}")
    public ProjectDto updateProject(@PathVariable Long id,
                                    @RequestParam String name){
        return projectService.updateProject(id, name);
    }

    @GetMapping("/api/projects/get_all")
    public List<ProjectDto> fetchProjects(@RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixname){
        return projectService.fetchProjects(optionalPrefixname);
    }


    @DeleteMapping("/api/projects/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }
}

