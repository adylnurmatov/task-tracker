package com.nurmatov.tasktracker.service.impl;

import com.nurmatov.tasktracker.dto.TaskDto;
import com.nurmatov.tasktracker.dto.TaskRequest;
import com.nurmatov.tasktracker.entity.Task;
import com.nurmatov.tasktracker.entity.TaskState;
import com.nurmatov.tasktracker.exception.NotFoundException;
import com.nurmatov.tasktracker.mapper.TaskMapper;
import com.nurmatov.tasktracker.repository.TaskRepository;
import com.nurmatov.tasktracker.repository.TaskStateRepository;
import com.nurmatov.tasktracker.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskStateRepository taskStateRepository;
    private final TaskMapper taskMapper;


    @Override
    public List<TaskDto> getTasks(Long taskStateId) {
        TaskState taskState = taskStateRepository.findById(taskStateId).orElseThrow(() -> new NotFoundException("Task state not found"));
        List<TaskDto> taskDtos = taskMapper.toDtos(taskState.getTasks());
        return taskDtos;
    }

    @Override
    public TaskDto createTask(Long taskStateId, TaskRequest request) {
        TaskState taskState = taskStateRepository.findById(taskStateId).orElseThrow(() -> new NotFoundException("Task state not found"));
        Task task = taskRepository.saveAndFlush(
                Task.builder()
                        .name(request.getName())
                        .desctiption(request.getDescription())
                        .taskState(taskState)
                        .build()
        );
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto updateTaskDto(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        taskRepository.saveAndFlush(
                Task.builder()
                        .id(task.getId())
                        .name(request.getName())
                        .desctiption(request.getDescription())
                        .taskState(task.getTaskState())
                        .build()
        );
        return taskMapper.toDto(task);
    }

    @Override
    public void deleteTaskDto(Long id) {
        taskRepository.deleteById(id);
    }
}
