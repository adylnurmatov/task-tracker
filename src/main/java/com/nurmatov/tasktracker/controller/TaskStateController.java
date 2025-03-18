package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.TaskStateDto;
import com.nurmatov.tasktracker.service.TaskStateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@AllArgsConstructor
public class TaskStateController {
    private final TaskStateService taskStateService;


    private static final String GET_TASK_STATES = "/api/projects/{project_id}/task_states";
    private static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task_states";

    private static final String UPDATE_TASK_STATE = "/api/task_states/{task_state_id}";
    private static final String DELETE_TASK_STATE = "/api/task_states/{task_state_id}";


    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable(name = "project_id") Long projectId) {
        return taskStateService.getTaskStates(projectId);
    }


    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(@PathVariable(name = "project_id") Long projectId,
                                        @RequestParam(name = "task_state_name") String name) {

        return taskStateService.createTaskState(projectId, name);
    }

    @PatchMapping(UPDATE_TASK_STATE)
    public TaskStateDto updateTaskStateDto(@PathVariable(name = "task_state_id") Long taskStateId,
                                           @RequestParam(name = "task_state_name") String name) {
        return taskStateService.updateTaskStateDto(taskStateId, name);
    }


    @DeleteMapping(DELETE_TASK_STATE)
    public void deleteTaskStateDto(@PathVariable(name = "task_state_id") Long id) {
        taskStateService.deleteTaskStateDto(id);
    }


}
