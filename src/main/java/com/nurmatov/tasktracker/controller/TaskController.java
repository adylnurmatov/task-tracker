package com.nurmatov.tasktracker.controller;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.dto.TaskRequest;
import com.nurmatov.tasktracker.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@AllArgsConstructor
public class TaskController {


    private final TaskService taskService;


    private static final String GET_TASKS = "/api/task_states/{task_state_id}/tasks";
    private static final String CREATE_TASK = "/api/task_states/{task_state_id}/task";

    private static final String UPDATE_TASK = "/api/tasks/{task_id}";
    private static final String DELETE_TASK = "/api/tasks/{task_id}";


    @GetMapping(GET_TASKS)
    public List<TaskDto> getTask(@PathVariable(name = "task_state_id") Long taskStateId) {
        return taskService.getTasks(taskStateId);
    }


    @PostMapping(CREATE_TASK)
    public TaskDto createTask(@PathVariable(name = "task_state_id") Long taskStateId,
                              @RequestBody TaskRequest taskRequest) {

        return taskService.createTask(taskStateId, taskRequest);
    }

    @PatchMapping(UPDATE_TASK)
    public TaskDto updateTaskDto(@PathVariable(name = "task_id") Long taskId,
                                 @RequestBody TaskRequest taskRequest) {
        return taskService.updateTaskDto(taskId, taskRequest);
    }


    @DeleteMapping(DELETE_TASK)
    public void deleteTaskDto(@PathVariable(name = "task_id") Long id) {
        taskService.deleteTaskDto(id);
    }
}
