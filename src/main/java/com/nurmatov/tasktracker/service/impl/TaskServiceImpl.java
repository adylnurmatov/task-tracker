package com.nurmatov.tasktracker.service.impl;

import com.nurmatov.tasktracker.mapper.TaskMapper;
import com.nurmatov.tasktracker.repository.TaskRepository;
import com.nurmatov.tasktracker.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
}
